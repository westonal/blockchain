package info.blockchain.challenge

import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import org.amshove.kluent.`should equal`
import org.junit.Test

class TransactionMappingTests {

    @Test
    fun `can map a positive value from an api transaction`() {
        val transaction: TransactionCardViewModel =
                Transaction(result = 12345678L).mapToViewModel(Mapper("xpub"))
        transaction.value `should equal` 12345678L.satoshiToBtc()
    }

    @Test
    fun `can map a negative value from an api transaction`() {
        val transaction: TransactionCardViewModel =
                Transaction(result = -123456789L).mapToViewModel(Mapper("xpub"))
        transaction.value `should equal` (-123456789L).satoshiToBtc()
    }

}