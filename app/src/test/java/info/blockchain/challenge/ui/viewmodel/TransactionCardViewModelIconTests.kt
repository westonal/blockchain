package info.blockchain.challenge.ui.viewmodel

import info.blockchain.challenge.R
import org.amshove.kluent.`should equal`
import org.junit.Test


class TransactionCardViewModelIconTests {

    @Test
    fun `a positive value shows the received icon`() {
        givenTransactionOfValue(1L).icon `should equal` R.drawable.ic_received_24dp
    }

    @Test
    fun `a negative value shows the sent icon`() {
        givenTransactionOfValue(-1L).icon `should equal` R.drawable.ic_sent_24dp
    }

    private fun givenTransactionOfValue(value: Long) = TransactionCardViewModel(value.satoshiToBtc())

}