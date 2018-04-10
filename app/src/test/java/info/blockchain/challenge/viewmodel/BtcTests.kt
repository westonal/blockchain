package info.blockchain.challenge.viewmodel

import info.blockchain.challenge.ui.viewmodel.satoshiToBtc
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test

class BtcTests {

    @Test
    fun `zero`() {
        0L.satoshiToBtc().toString() `should equal` "0.00000000 BTC"
    }

    @Test
    fun `0 satoshi is not positive`() {
        0L.satoshiToBtc().isPositive `should be` false
    }

    @Test
    fun `1 satoshi`() {
        1L.satoshiToBtc().toString() `should equal` "0.00000001 BTC"
    }

    @Test
    fun `1 satoshi is positive`() {
        1L.satoshiToBtc().isPositive `should be` true
    }

    @Test
    fun `12345678 satoshi`() {
        12345678L.satoshiToBtc().toString() `should equal` "0.12345678 BTC"
    }

    @Test
    fun `12345678 satoshi is positive`() {
        12345678L.satoshiToBtc().isPositive `should be` true
    }

    @Test
    fun `123456789 satoshi`() {
        123456789L.satoshiToBtc().toString() `should equal` "1.23456789 BTC"
    }

    @Test
    fun `1234567890 satoshi`() {
        1234567890L.satoshiToBtc().toString() `should equal` "12.34567890 BTC"
    }

    @Test
    fun `all the satoshi`() {
        21_000_000_0000_0123L.satoshiToBtc().toString() `should equal` "21000000.00000123 BTC"
    }

    @Test
    fun `-1 satoshi`() {
        (-1L).satoshiToBtc().toString() `should equal` "-0.00000001 BTC"
    }

    @Test
    fun `-1 satoshi is not positive`() {
        (-1L).satoshiToBtc().isPositive `should be` false
    }

    @Test
    fun `-100 satoshi is not positive`() {
        (-100L).satoshiToBtc().isPositive `should be` false
    }

    @Test
    fun `-1 bitcoin`() {
        (-1_0000_0000L).satoshiToBtc().toString() `should equal` "-1.00000000 BTC"
    }

}