package info.blockchain.challenge.ui.viewmodel

import org.amshove.kluent.`should equal`
import org.junit.Test
import java.util.*

class TransactionCardViewModelDateTimeTests {

    @Test
    fun `null date time formatting`() {
        Locale.setDefault(Locale.UK)
        givenTransactionOfDateTime(null)
                .dateTime `should equal` ""
    }

    @Test
    fun `date time formatted to the locale US`() {
        Locale.setDefault(Locale.US)
        givenTransactionOfDateTime(
                date(2018, 4, 11, 12, 23, 45))
                .dateTime `should equal` "Apr 11, 2018 12:23:45 PM"
    }

    @Test
    fun `date time formatted to the locale UK`() {
        Locale.setDefault(Locale.UK)
        givenTransactionOfDateTime(
                date(2017, 12, 31, 8, 2, 1))
                .dateTime `should equal` "31-Dec-2017 08:02:01"
    }

    private fun date(year: Int, month: Int, day: Int, hour: Int = 0, minute: Int = 0, second: Int = 0) =
            Calendar.getInstance().apply {
                set(year, month - 1, day, hour, minute, second)
            }.time

    private fun givenTransactionOfDateTime(date: Date?) = TransactionCardViewModel(
            id = 1,
            value = 0L.satoshiToBtc(),
            date = date
    )

}