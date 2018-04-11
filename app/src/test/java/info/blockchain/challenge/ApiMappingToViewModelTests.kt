package info.blockchain.challenge

import info.blockchain.challenge.api.Result
import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.api.Wallet
import info.blockchain.challenge.ui.viewmodel.AccountCardViewModel
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.amshove.kluent.`should not be`
import org.junit.Test

typealias TransactionVm = info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel

class ResultMappingTests {

    @Test
    fun `map result to Card view models`() {
        val onlyCard = Result(
                wallet = Wallet(finalBalance = 1234L),
                transactions = emptyList()
        ).mapToCardViewModels().single()
        (onlyCard as AccountCardViewModel).finalBalance `should equal` 1234L.satoshiToBtc()
    }

    @Test
    fun `map result with a transaction to two Card view models`() {
        val cards = Result(
                wallet = Wallet(finalBalance = 12345L),
                transactions = listOf(Transaction(result = 9999L))
        ).mapToCardViewModels()
        cards.size `should be` 2
        (cards.first() as AccountCardViewModel).finalBalance `should equal` 12345L.satoshiToBtc()
        (cards.last() as TransactionVm).value `should equal` 9999L.satoshiToBtc()
    }

    @Test
    fun `map result with a transaction to three Card view models`() {
        val cards = Result(
                wallet = Wallet(finalBalance = 1L),
                transactions = listOf(
                        Transaction(result = 1234L),
                        Transaction(result = 5678L)
                )
        ).mapToCardViewModels()
        cards.size `should be` 3
        (cards[0] as AccountCardViewModel).finalBalance `should equal` 1L.satoshiToBtc()
        (cards[1] as TransactionVm).value `should equal` 1234L.satoshiToBtc()
        (cards[2] as TransactionVm).value `should equal` 5678L.satoshiToBtc()
    }

    @Test
    fun `can map timestamp`() {
        val cards = Result(
                wallet = Wallet(finalBalance = 12345L),
                transactions = listOf(
                        Transaction(timeStamp = 1523447258L),
                        Transaction(timeStamp = 1491868800L)
                )
        ).mapToCardViewModels()
        cards.size `should be` 3
        cards[1] as TransactionVm `should have time stamp` 1523447258_000L
        cards[2] as TransactionVm `should have time stamp` 1491868800_000L
    }
}

private infix fun TransactionVm.`should have time stamp`(expectedTimeStamp: Long) {
    this.date `should not be` null
    this.date!!.time `should equal` expectedTimeStamp
}