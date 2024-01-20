package com.example.deamhome.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.crashlytics.KeyValueBuilder
import com.google.firebase.crashlytics.setCustomKeys

fun reportNonSeriousException(e: Exception, reportValues: KeyValueBuilder.() -> Unit) {
    val crashlytics = FirebaseCrashlytics.getInstance()
    crashlytics.setCustomKeys(reportValues)
    crashlytics.recordException(e)
}
