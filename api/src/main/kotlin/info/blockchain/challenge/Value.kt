package info.blockchain.challenge

import java.math.BigDecimal

class Value(satoshis: Long) {

    private val bitcoinValue = BigDecimal(satoshis).movePointLeft(8)

    override fun toString(): String {
        return "${bitcoinValue.toPlainString()} BTC"
    }
}