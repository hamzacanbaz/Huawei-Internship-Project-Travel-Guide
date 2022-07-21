package com.canbazdev.hmskitsproject1.util

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter

/*
*   Created by hamzacanbaz on 7/20/2022
*/

@BindingAdapter("android:setPostImage")
fun setPostImage(view:ImageView, uri: Uri){
    view.setImageURI(uri)
}