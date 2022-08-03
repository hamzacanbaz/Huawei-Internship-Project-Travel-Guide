package com.canbazdev.hmskitsproject1.util

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.canbazdev.hmskitsproject1.R

/*
*   Created by hamzacanbaz on 7/20/2022
*/

@BindingAdapter("android:setPostImage")
fun setPostImage(view: ImageView, uri: Uri) {
    view.setImageURI(uri)
}

@BindingAdapter("android:loadImage")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) Glide.with(view.context).load(url).centerCrop().into(view)
}

@BindingAdapter("android:loadQrImage")
fun loadQrImage(view: ImageView, url: String?) {
    if (url != null) Glide.with(view.context).load(url).into(view)
}

@BindingAdapter("android:loadImageWithRadius")
fun loadImageWithRadius(view: ImageView, url: String?) {
    if (url != null) Glide.with(view.context).load(url).transform(RoundedCorners(20)).centerCrop()
        .into(view)
}

@BindingAdapter("android:setButtonEnabled")
fun setButtonEnabled(view: TextView, imageUrl: Boolean) {
    if (imageUrl) {
        view.isEnabled = true
        view.setTextColor(ContextCompat.getColor(view.context, R.color.primary_dark));
    } else {
        view.isEnabled = false
        view.setTextColor(ContextCompat.getColor(view.context, R.color.gray));
    }
}
