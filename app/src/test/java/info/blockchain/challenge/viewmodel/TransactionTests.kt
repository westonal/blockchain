package info.blockchain.challenge.viewmodel

import info.blockchain.challenge.R
import info.blockchain.challenge.ui.viewmodel.Transaction
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import org.amshove.kluent.`should equal`
import org.junit.Test

class TransactionTests {

    @Test
    fun `a positive value shows the received icon`() {
        givenTransactionOfValue(1L).icon `should equal` R.drawable.ic_received_24dp
    }

    @Test
    fun `a negative value shows the sent icon`() {
        givenTransactionOfValue(-1L).icon `should equal` R.drawable.ic_sent_24dp
    }

    private fun givenTransactionOfValue(value: Long) = Transaction(value.satoshiToBtc())

}