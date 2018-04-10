package info.blockchain.challenge

import org.amshove.kluent.`should equal`
import org.junit.Test

class ValueTests {

    @Test
    fun `zero`() {
        Value(0).toString() `should equal` "0.00000000 BTC"
    }

    @Test
    fun `1 satoshi`() {
        Value(1L).toString() `should equal` "0.00000001 BTC"
    }

    @Test
    fun `12345678 satoshi`() {
        Value(12345678L).toString() `should equal` "0.12345678 BTC"
    }

    @Test
    fun `123456789 satoshi`() {
        Value(123456789L).toString() `should equal` "1.23456789 BTC"
    }

    @Test
    fun `1234567890 satoshi`() {
        Value(1234567890L).toString() `should equal` "12.34567890 BTC"
    }

    @Test
    fun `all the satoshi`() {
        Value(21_000_000_0000_0123L).toString() `should equal` "21000000.00000123 BTC"
    }

    @Test
    fun `-1 satoshi`() {
        Value(-1L).toString() `should equal` "-0.00000001 BTC"
    }

    @Test
    fun `-1 bitcoin`() {
        Value(-1_0000_0000L).toString() `should equal` "-1.00000000 BTC"
    }
}