package info.blockchain.challenge.ui.viewmodel

import android.support.annotation.DrawableRes
import info.blockchain.challenge.R

// Note: The sealed class gives me the ability to use an exhaustive "when" block over the type, i.e. knowing I've
// covered all of the subtypes
sealed class Card

class Account(
        val finalBalance: Btc
) : Card()

class Transaction(
        // Note: Here I'm preferring to use a strong type, not sticking to the Long from the api call
        val value: Btc,
        val address: String = ""
) : Card() {
    // Note: the Res annotation can help catch errors, e.g. attempting using this where a String id is expected.
    @DrawableRes
    val icon: Int = if (value.isPositive) R.drawable.ic_received_24dp else R.drawable.ic_sent_24dp
}