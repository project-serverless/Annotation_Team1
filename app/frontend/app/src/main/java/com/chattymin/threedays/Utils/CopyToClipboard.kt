package com.chattymin.threedays.Utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

fun CopyToClipboard(context: Context, text: String) {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text", text)
    clipboardManager.setPrimaryClip(clipData)
}