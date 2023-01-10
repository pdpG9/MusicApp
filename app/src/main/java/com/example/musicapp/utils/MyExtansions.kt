package com.example.musicapp.utils

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.widget.SeekBar
import timber.log.Timber
import java.util.concurrent.TimeUnit

fun Window.changeStatusBarColor(colorId: Int) {
    this.statusBarColor = this.context.getColor(colorId)
}

fun String.myLog(tag: String = "TTT") {
    Timber.tag(tag).d(this)
    Log.d("TTT", "myLog: $this")
}

fun <T> T.myApply(block: T.() -> Unit) {
    block(this)
}

fun ViewGroup.myInflate(resId: Int) = LayoutInflater.from(this.context).inflate(resId, this, false)

fun SeekBar.setChangeProgress(block: (Int) -> Unit) {
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

        }

        override fun onStartTrackingTouch(p0: SeekBar?) {

        }

        override fun onStopTrackingTouch(p0: SeekBar?) {
            p0?.let {
                block.invoke(it.progress)
            }
        }
    })
}

fun Long.convertToMMSS(): String {
    return String.format(
        "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(this) % TimeUnit.HOURS.toMinutes(1),
        TimeUnit.MILLISECONDS.toSeconds(this) % TimeUnit.MINUTES.toSeconds(1)
    )
}
