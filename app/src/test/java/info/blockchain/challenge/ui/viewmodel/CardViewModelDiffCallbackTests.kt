package info.blockchain.challenge.ui.viewmodel

import org.amshove.kluent.`should be`
import org.junit.Test

class CardViewModelDiffCallbackTests {

    @Test
    fun `Can identify two identical transactions - same content`() {
        CardViewModelDiffCallback(
                listOf(transaction(1)),
                listOf(transaction(1)))
                .areContentsTheSame(0, 0) `should be` true
    }

    @Test
    fun `Can identify two non-identical transactions - same content`() {
        CardViewModelDiffCallback(
                listOf(transaction(1)),
                listOf(transaction(2)))
                .areContentsTheSame(0, 0) `should be` false
    }

    @Test
    fun `Can identify two identical transactions - same items`() {
        CardViewModelDiffCallback(
                listOf(transaction(1)),
                listOf(transaction(1)))
                .areItemsTheSame(0, 0) `should be` true
    }

    @Test
    fun `Can identify two non-identical transactions - same items`() {
        CardViewModelDiffCallback(
                listOf(transaction(1)), listOf(transaction(2)))
                .areContentsTheSame(0, 0) `should be` false
    }

    @Test
    fun `Can identify two identical transactions in different positions - same content`() {
        CardViewModelDiffCallback(
                listOf(transaction(1), transaction(2), transaction(3)),
                listOf(transaction(10), transaction(9), transaction(2)))
                .areContentsTheSame(1, 2) `should be` true
    }

    @Test
    fun `Can identify two identical transactions in different positions - same items`() {
        CardViewModelDiffCallback(
                listOf(transaction(1), transaction(2), transaction(3)),
                listOf(transaction(10), transaction(9), transaction(2)))
                .areItemsTheSame(1, 2) `should be` true
    }

    @Test
    fun `Can identify two non-identical transactions in different positions - same items`() {
        CardViewModelDiffCallback(
                listOf(transaction(1), transaction(2), transaction(3)),
                listOf(transaction(10), transaction(9), transaction(1)))
                .areItemsTheSame(1, 2) `should be` false
    }

    @Test
    fun `Can identify two non-identical transactions in different positions - same content`() {
        CardViewModelDiffCallback(
                listOf(transaction(1), transaction(2), transaction(3)),
                listOf(transaction(10), transaction(9), transaction(1)))
                .areContentsTheSame(1, 2) `should be` false
    }

    @Test
    fun `Old list count`() {
        CardViewModelDiffCallback(
                listOf(transaction(1)),
                emptyList())
                .oldListSize `should be` 1
    }

    @Test
    fun `New list count`() {
        CardViewModelDiffCallback(
                emptyList(),
                listOf(transaction(1), transaction(2), transaction(3)))
                .newListSize `should be` 3
    }

    private fun transaction(id: Long) = TransactionCardViewModel(id = id, value = 0L.satoshiToBtc())

}