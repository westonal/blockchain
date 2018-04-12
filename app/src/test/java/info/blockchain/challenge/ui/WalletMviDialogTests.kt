package info.blockchain.challenge.ui

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.verifyNoMoreInteractions
import com.nhaarman.mockito_kotlin.verifyZeroInteractions
import info.blockchain.challenge.R
import info.blockchain.challenge.api.MultiAddress
import info.blockchain.challenge.api.Result
import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.api.Wallet
import info.blockchain.challenge.applyAssertsToValue
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel
import io.reactivex.Single
import org.amshove.kluent.`it returns`
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.any
import org.junit.Test

class WalletMviDialogTests {

    @Test
    fun `given no xpubs, no view models are created and service is not called`() {
        val service: MultiAddress = mock()
        WalletMviDialog(service)
                .cardViewModels.test().assertNoValues()
        verifyZeroInteractions(service)
    }

    @Test
    fun `given a single xpub, the service is called and result is mapped to a view model`() {
        val xpub = "xpub12345"
        val service: MultiAddress = givenServiceReturns(xpub, Transaction(result = 2L))
        val walletModule = WalletMviDialog(service)
        val testObserver = walletModule
                .cardViewModels.test()

        walletModule.xpub = xpub

        testObserver.applyAssertsToValue { viewModel -> viewModel.size `should be` 2 }
    }

    @Test
    fun `given a single xpub, the service is called just once`() {
        val xpub = "xpub12345"
        val service: MultiAddress = givenServiceReturns(xpub)
        val walletModule = WalletMviDialog(service)
        walletModule.cardViewModels.test()
        walletModule.xpub = xpub
        verify(service).multiaddr(xpub)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `given the same xpub twice, the service is called just once`() {
        val xpub = "xpub12345"
        val service: MultiAddress = givenServiceReturns(xpub)
        val walletModule = WalletMviDialog(service)

        walletModule.cardViewModels.test()
        walletModule.xpub = xpub
        walletModule.xpub = xpub

        verify(service).multiaddr(xpub)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `given two xpubs, the service is called twice`() {
        val xpub1 = "xpub12345"
        val xpub2 = "xpub23456"
        val service: MultiAddress = mock {
            on { it.multiaddr(xpub1) } `it returns` apiResult()
            on { it.multiaddr(xpub2) } `it returns` apiResult()
        }
        val walletModule = WalletMviDialog(service)

        walletModule.cardViewModels.test()
        walletModule.xpub = xpub1
        walletModule.xpub = xpub2

        verify(service).multiaddr(xpub1)
        verify(service).multiaddr(xpub2)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun `given a single xpub, the service is called zero times if not subscribed to`() {
        val xpub = "xpub12345"
        val service: MultiAddress = givenServiceReturns(xpub)
        WalletMviDialog(service).xpub = xpub
        verifyZeroInteractions(service)
    }

    @Test
    fun `given a single xpub, the service is called and result with 2 transactions is mapped to a view model`() {
        val xpub = "xpub23456"
        val service: MultiAddress = givenServiceReturns(xpub, Transaction(result = 2L), Transaction(result = 3L))
        val walletModule = WalletMviDialog(service)
        val testObserver = walletModule
                .cardViewModels.test()

        walletModule.xpub = xpub

        testObserver.assertNotComplete()

        testObserver.applyAssertsToValue { viewModel -> viewModel.size `should be` 3 }
    }

    @Test
    fun `if the service throws an exception, a single ErrorCard is shown`() {
        val xpub = "xpub23456"
        val service: MultiAddress = givenServiceThrows()
        val walletModule = WalletMviDialog(service)
        val testObserver = walletModule
                .cardViewModels.test()

        walletModule.xpub = xpub

        testObserver.applyAssertsToValue { viewModel ->
            viewModel.size `should be` 1
            viewModel[0] `should be instance of` ErrorCardViewModel::class
            (viewModel[0] as ErrorCardViewModel).message `should equal` R.string.generic_error_retry
        }
    }

    @Test
    fun `when there is an error, can retry`() {
        val xpub = "xpub23456"
        val service: MultiAddress = mock {
            on { multiaddr(any()) }
                    .`it returns`(Single.error { RuntimeException() })
                    // Note: set up to fail once, then succeed
                    .`it returns`(apiResult(Transaction(result = 2L)))
        }
        val walletModule = WalletMviDialog(service)
        val testObserver = walletModule
                .cardViewModels.test()

        walletModule.xpub = xpub

        testObserver.assertNotComplete()

        testObserver.applyAssertsToValue { viewModel ->
            (viewModel[0] as ErrorCardViewModel).executeRetry()
        }

        testObserver.applyAssertsToValue(1) { viewModel ->
            viewModel.size `should be` 2
            viewModel[1] `should be instance of` TransactionCardViewModel::class
        }

        testObserver.valueCount() `should be` 2
        verify(service, times(2)).multiaddr(xpub)
    }

    @Test
    fun `can be refreshed`() {
        val xpub = "xpub34567"
        val service: MultiAddress = mock {
            on { multiaddr(any()) }
                    .`it returns`(apiResult(Transaction(result = 2L)))
                    // Note: set up to return with different results
                    .`it returns`(apiResult(Transaction(result = 1L), Transaction(result = 2L)))
        }
        val walletModule = WalletMviDialog(service)
        val testObserver = walletModule
                .cardViewModels.test()

        walletModule.xpub = xpub
        walletModule.refresh()

        testObserver.assertNotComplete()

        testObserver.applyAssertsToValue { viewModel ->
            viewModel.size `should be` 2
            viewModel[1] `should be instance of` TransactionCardViewModel::class
        }

        testObserver.applyAssertsToValue(1) { viewModel ->
            viewModel.size `should be` 3
            viewModel[1] `should be instance of` TransactionCardViewModel::class
        }

        testObserver.valueCount() `should be` 2
        verify(service, times(2)).multiaddr(xpub)
    }

    private fun givenServiceThrows(): MultiAddress = mock {
        on { multiaddr(any()) } `it returns` Single.error<Result> { RuntimeException() }
    }

    private fun givenServiceReturns(xpub: String, vararg transactions: Transaction): MultiAddress {
        return mock {
            on { it.multiaddr(xpub) } `it returns` apiResult(*transactions)
        }
    }

    private fun apiResult(vararg transactions: Transaction) =
            Single.just(Result(Wallet(1L),
                    transactions = listOf(*transactions)))

}

