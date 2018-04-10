package info.blockchain.challenge

import info.blockchain.challenge.api.Result
import info.blockchain.challenge.api.TXO
import info.blockchain.challenge.ui.viewmodel.AccountCardViewModel
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc

// Note: These aliases make the code clearer due to the name clash. Few places will need both, and this prevents us
// resorting to smurf-naming like: info.blockchain.challenge.api.TransactionApi
typealias ApiTransaction = info.blockchain.challenge.api.Transaction

typealias ViewModelTransaction = TransactionCardViewModel

fun ApiTransaction.mapToViewModel() =
        ViewModelTransaction(
                value = this.result.satoshiToBtc(),
                address = this.outputs.findFirstNonWalletAddress()
        )

fun Result.mapToCardViewModels(): List<CardViewModel> =
        listOf(mapToAccountViewModel()) + this.transactions.map(ApiTransaction::mapToViewModel)

private fun Result.mapToAccountViewModel() = AccountCardViewModel(this.wallet.finalBalance.satoshiToBtc())

private fun List<TXO>.findFirstNonWalletAddress() =
        this.firstNonWalletTransactionOutput()?.address ?: ""

private fun List<TXO>.firstNonWalletTransactionOutput() =
        this.firstOrNull { !it.isWalletAddress() }

private fun TXO.isWalletAddress() = this.xpub != null
