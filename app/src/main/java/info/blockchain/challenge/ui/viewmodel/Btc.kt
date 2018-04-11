package info.blockchain.challenge.ui.viewmodel

import java.math.BigDecimal

data class Btc internal constructor(private val satoshis: Long) {

    private val bitcoinValue = BigDecimal(satoshis).movePointLeft(8)

    override fun toString(): String {
        return "${bitcoinValue.toPlainString()} BTC"
    }

    val isPositive: Boolean = satoshis > 0
}

// Note: the extension function makes it more readable like a static factory method, but also fluent
fun Long.satoshiToBtc() = Btc(this)