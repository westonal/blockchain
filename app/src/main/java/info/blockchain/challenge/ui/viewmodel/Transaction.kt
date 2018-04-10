package info.blockchain.challenge.ui.viewmodel

import android.support.annotation.DrawableRes
import info.blockchain.challenge.R

class Transaction(
        // Note: Here I'm preferring to use a strong type, not sticking to the Long from the api call
        val value: Btc
) {
    // Note: the Res annotation can help catch errors, e.g. attempting using this where a String id is expected.
    @DrawableRes
    val icon: Int = if (value.isPositive) R.drawable.ic_received_24dp else R.drawable.ic_sent_24dp
}