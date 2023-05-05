package com.drsync.core.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.material.textfield.TextInputLayout

object ConstantFunction {
    fun Activity.changeStatusbarColor(isWhiteBg: Boolean, color: Int) {
        val window: Window = this.window
        val decorView: View = window.decorView
        val wic = WindowInsetsControllerCompat(window, decorView)
        wic.isAppearanceLightStatusBars = isWhiteBg

        window.statusBarColor = color
    }

    fun isUsingNightModeResources(resources: Resources): Boolean {
        return when (resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            Configuration.UI_MODE_NIGHT_UNDEFINED -> false
            else -> false
        }
    }

    fun Context.createProgress(): CircularProgressDrawable {
        return CircularProgressDrawable(this).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
    }

    fun Int.dpToInt(context: Context): Int {
        val scale = context.resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    fun TextInputLayout.setInputError(message: String): Boolean {
        this.isErrorEnabled = true
        this.error = message
        this.requestFocus()
        return false
    }

    fun AppCompatActivity.playPopupAnimation() {
        this.overridePendingTransition(
            com.drsync.core.R.anim.from_bottom,
            com.drsync.core.R.anim.no_animation
        )
    }

    fun fibonacci(n: Int, a: Int = 0, b: Int = 1): Int =
        when (n) {
            0 -> a
            1 -> b
            else -> fibonacci(n - 1, b, a + b)
        }
}