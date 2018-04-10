package info.blockchain.challenge

import info.blockchain.challenge.ui.viewmodel.Transaction
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import org.amshove.kluent.`should equal`
import org.junit.Test

typealias ApiTransaction = info.blockchain.challenge.api.Transaction

class TransactionMappingTests {

    @Test
    fun `can map a positive value from an api transaction`() {
        val transaction: Transaction = ApiTransaction(result = 12345678L).mapToViewModel()
        transaction.value `should equal` 12345678L.satoshiToBtc()
    }

    @Test
    fun `can map a negative value from an api transaction`() {
        val transaction: Transaction = ApiTransaction(result = -123456789L).mapToViewModel()
        transaction.value `should equal` (-123456789L).satoshiToBtc()
    }

    @Test
    fun `a positive value shows the received icon`() {
        val transaction: Transaction = ApiTransaction(result = 1L).mapToViewModel()
        transaction.icon `should equal` R.drawable.ic_received_24dp
    }

    @Test
    fun `a negative value shows the sent icon`() {
        val transaction: Transaction = ApiTransaction(result = -1L).mapToViewModel()
        transaction.icon `should equal` R.drawable.ic_sent_24dp
    }

}