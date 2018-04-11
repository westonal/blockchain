package info.blockchain.challenge.ui

import android.databinding.BindingAdapter
import android.support.annotation.DrawableRes
import android.widget.ImageView

// Note: Without this, we can't bind a res ID from the VM to an ImageView src
@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, @DrawableRes resource: Int) {
    imageView.setImageResource(resource)
}
