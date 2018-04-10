package info.blockchain.challenge

import info.blockchain.challenge.api.TXO
import info.blockchain.challenge.ui.viewmodel.Transaction
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc

// Note: These aliases make the code clearer due to the name clash. Few places will need both, and this prevents us
// resorting to smurf-naming like: info.blockchain.challenge.api.TransactionApi
typealias ApiTransaction = info.blockchain.challenge.api.Transaction

typealias ViewModelTransaction = Transaction

fun ApiTransaction.mapToViewModel() =
        ViewModelTransaction(
                value = this.result.satoshiToBtc(),
                address = this.outputs.findFirstNonWalletAddress()
        )

private fun List<TXO>.findFirstNonWalletAddress() =
        this.firstNonWalletTransactionOutput()?.address ?: ""

private fun List<TXO>.firstNonWalletTransactionOutput() =
        this.firstOrNull { !it.isWalletAddress() }

private fun TXO.isWalletAddress() = this.xpub != null
