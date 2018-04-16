package info.blockchain.challenge

import info.blockchain.challenge.api.Transaction
import org.amshove.kluent.`should equal`
import org.junit.Test

class TransactionHashMappingTests {

    @Test
    fun `can map a hash as the id`() {
        Transaction(hash = "7FFFFFFFFFFFFFFFFFFFFFFFFFFF") `once mapped should have id` 0x7FFFFFFFFFFFFFFFL
    }

    @Test
    fun `can map a hash as the id over 7f`() {
        @Suppress("INTEGER_OVERFLOW")
        Transaction(hash = "8000000000000000000000000000") `once mapped should have id` 0x7FFFFFFFFFFFFFFFL + 1L
    }

    @Test
    fun `can map a hash as the id - max`() {
        Transaction(hash = "FFFFFFFFFFFFFFFFFFFFFFF") `once mapped should have id` -1
    }

    @Test
    fun `can map a hash as the id - all chars`() {
        Transaction(hash = "1234567890ABCDEF") `once mapped should have id` 0x1234567890ABCDEFL
    }

    @Test
    fun `can map a hash as the id - all chars lowercase`() {
        Transaction(hash = "1234567890abcdef") `once mapped should have id` 0x1234567890ABCDEFL
    }

    @Test
    fun `if hash is not hex, revert to timestamp`() {
        Transaction(hash = "Not hex", timeStamp = 3456) `once mapped should have id` 3456
    }

    @Test
    fun `if hash is not hex, but long, revert to timestamp`() {
        Transaction(hash = "Not hex but still over 16 chars", timeStamp = 3456) `once mapped should have id` 3456
    }

    @Test
    fun `when missing a hash, uses the timestamp`() {
        Transaction(timeStamp = 1234567890) `once mapped should have id` 1234567890
    }

    private infix fun Transaction.`once mapped should have id`(expected: Long) {
        mapToViewModel(Mapper("xpub")).id `should equal` expected
    }

}