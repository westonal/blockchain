package info.blockchain.challenge

import info.blockchain.challenge.api.TXO
import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.api.Xpub
import org.amshove.kluent.`should equal`
import org.junit.Test

class TransactionAddressMappingTests {

    @Test
    fun `empty address`() {
        val transaction = Transaction().mapToViewModel(Mapper("xpub"))
        transaction.address `should equal` ""
    }

    @Test
    fun `single output with null xpub`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(
                                address = "1LgE7gy6Tq9VKbV4FhsLHYA6QrMP8eJXrJ",
                                xpub = null
                        )
                )).mapToViewModel(Mapper("xpub"))
        transaction.address `should equal` "1LgE7gy6Tq9VKbV4FhsLHYA6QrMP8eJXrJ"
    }

    @Test
    fun `single output with xpub that matches the wallet`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(
                                address = "1AP6CyqYVhEcPMqYCReHoAFVZaVLWAKaJN",
                                xpub = Xpub("Xpub123")
                        )
                )).mapToViewModel(Mapper("Xpub123"))
        transaction.address `should equal` ""
    }

    @Test
    fun `single output with xpub that doesn't match the wallet`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(
                                address = "1AP6CyqYVhEcPMqYCReHoAFVZaVLWAKaJN",
                                xpub = Xpub("Xpub123")
                        )
                )).mapToViewModel(Mapper("Xpub456"))
        transaction.address `should equal` "1AP6CyqYVhEcPMqYCReHoAFVZaVLWAKaJN"
    }

    @Test
    fun `two outputs, first with xpub`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(address = "15snCyagJkvpaQGY5hMfcZkgvPDwf6FneP",
                                xpub = Xpub("Xpub123")),
                        TXO(address = "1NVMi1Ez5uTc3D84cXhNeaksrd4qmYq2rf",
                                xpub = null)
                )
        ).mapToViewModel(Mapper("Xpub123"))
        transaction.address `should equal` "1NVMi1Ez5uTc3D84cXhNeaksrd4qmYq2rf"
    }

    @Test
    fun `two outputs, first with non-matching xpub`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(address = "15snCyagJkvpaQGY5hMfcZkgvPDwf6FneP",
                                xpub = Xpub("Xpub123")),
                        TXO(address = "1NVMi1Ez5uTc3D84cXhNeaksrd4qmYq2rf",
                                xpub = null)
                )
        ).mapToViewModel(Mapper("Xpub456"))
        transaction.address `should equal` "15snCyagJkvpaQGY5hMfcZkgvPDwf6FneP"
    }

    // Note: this case is to show I have considered it, you might want to show both, or "multiple" but I just show
    // the first
    @Test
    fun `two outputs, both without xpub`() {
        val transaction = Transaction(
                outputs = listOf(
                        TXO(address = "1Q2XkxYnd56Saz6GnrJuuxhFDWNj6BVHrn",
                                xpub = null),
                        TXO(address = "195Ucqi4Rgx319LM8SEz8a7UFoMpbD7mid",
                                xpub = null)
                )
        ).mapToViewModel(Mapper("xpub"))
        transaction.address `should equal` "1Q2XkxYnd56Saz6GnrJuuxhFDWNj6BVHrn"
    }

}