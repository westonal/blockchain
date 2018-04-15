package info.blockchain.challenge

import info.blockchain.challenge.api.Result
import info.blockchain.challenge.api.TXO
import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.ui.viewmodel.AccountCardViewModel
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel
import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import java.util.*

fun Result.mapToCardViewModels(mapper: Mapper) = mapper.mapToCardViewModels(this)

fun Transaction.mapToViewModel(mapper: Mapper) = mapper.mapToViewModel(this)

class Mapper(private val walletXpub: String) {
    fun mapToViewModel(transaction: Transaction) =
            TransactionCardViewModel(
                    id = transaction.hashToALong(),
                    value = transaction.result.satoshiToBtc(),
                    address = transaction.outputs.findFirstNonChangeAddress(),
                    date = Date(transaction.timeStamp * 1000)
            )

    fun mapToCardViewModels(result: Result): List<CardViewModel> =
            listOf(result.mapToAccountViewModel()) + result.transactions.map { mapToViewModel(it) }

    private fun Result.mapToAccountViewModel() = AccountCardViewModel(this.wallet.finalBalance.satoshiToBtc())

    private fun List<TXO>.findFirstNonChangeAddress() =
            this.firstNonChangeTransactionOutput()?.address ?: ""

    private fun List<TXO>.firstNonChangeTransactionOutput() =
            this.firstOrNull { !it.sentToChangeAddress() }

    private fun TXO.sentToChangeAddress() = this.xpub?.m == walletXpub
}

private fun Transaction.hashToALong(): Long {
    if (this.hash.length > 15 && this.hash.isHex()) {
        //I need a Long for a hash, so I'm taking the first 16 chars of the longer transaction hash
        return java.lang.Long.parseLong(this.hash.substring(0, 2), 16).shl(56) +
                java.lang.Long.parseLong(this.hash.substring(2, 16), 16)
    }
    // This is a sensible fallback
    return this.timeStamp
}

private fun String.isHex() = all {
    return when (it) {
        in '0'..'9' -> true
        in 'a'..'f' -> true
        in 'A'..'F' -> true
        else -> false
    }
}
