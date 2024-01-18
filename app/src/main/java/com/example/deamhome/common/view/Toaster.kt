package com.example.deamhome.common.view

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object Toaster {
    private var toast: Toast? = null

    fun showShort(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT).apply { this.show() }
    }

    fun showLong(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG).apply { this.show() }
    }

    fun showShort(context: Context, @StringRes messageId: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, context.getString(messageId), Toast.LENGTH_SHORT)
            .apply { this.show() }
    }

    fun showLong(context: Context, @StringRes messageId: Int) {
        toast?.cancel()
        toast = Toast.makeText(context, context.getString(messageId), Toast.LENGTH_LONG)
            .apply { this.show() }
    }

    fun cancel() {
        toast?.cancel()
    }
}
