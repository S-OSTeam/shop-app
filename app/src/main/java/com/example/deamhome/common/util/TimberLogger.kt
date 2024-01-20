package com.example.deamhome.common.util

import timber.log.Timber

enum class LogLevel {
    D,
    W,
    E,
}

fun Any.log(message: String, logLevel: LogLevel = LogLevel.D) {
    when (logLevel) {
        LogLevel.D -> Timber.d("${this::class.simpleName}: $message")
        LogLevel.W -> Timber.w("${this::class.simpleName}: $message")
        LogLevel.E -> Timber.e("${this::class.simpleName}: $message")
    }
}

fun Any.log(tag: String? = null, message: String, logLevel: LogLevel = LogLevel.D) {
    if (tag == null) {
        log(message, logLevel)
    } else {
        when (logLevel) {
            LogLevel.D -> Timber.tag(tag).d("${this::class.simpleName}: $message")
            LogLevel.W -> Timber.tag(tag).w("${this::class.simpleName}: $message")
            LogLevel.E -> Timber.tag(tag).e("${this::class.simpleName}: $message")
        }
    }
}
