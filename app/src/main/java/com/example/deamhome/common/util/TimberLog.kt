package com.example.deamhome.common.util

import timber.log.Timber

fun Any.log(message: String) {
    Timber.d("${this::class.simpleName}: $message")
}
