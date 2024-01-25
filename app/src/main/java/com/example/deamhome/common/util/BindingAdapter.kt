package com.example.deamhome.common.util

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.deamhome.R
import com.google.android.material.chip.Chip

@BindingAdapter("imageUrl", "placeholder", requireAll = false)
fun ImageView.setImageUrl(url: String?, placeholder: Drawable? = null) {
    Glide.with(context)
        .load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .fallback(R.drawable.fallback_tmp_image)
        .into(this)
}

@BindingAdapter("imageUri", "placeholder", requireAll = false)
fun ImageView.setImageUrl(uri: Uri?, placeholder: Drawable? = null) {
    uri?.let {
        Glide.with(context)
            .load(it)
            .placeholder(placeholder)
            .error(placeholder)
            .fallback(R.drawable.fallback_tmp_image)
            .into(this)
    }
}

@BindingAdapter("isVisible")
fun View.isVisible(isVisible: Boolean) {
    this.isVisible = isVisible
}

@BindingAdapter("onCloseClick")
fun Chip.onCloseClick(onCloseClick: () -> Unit) {
    this.setOnCloseIconClickListener {
        onCloseClick()
    }
}

@BindingAdapter("setTextOrEmpty")
fun TextView.setTextOrEmpty(text: String?) {
    this.text = text ?: ""
}

@BindingAdapter("isSelected")
fun View.isSelected(isSelected: Boolean) {
    this.isSelected = isSelected
}
