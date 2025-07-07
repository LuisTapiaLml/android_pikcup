package com.luisptapia.pickupandroid.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

object DialogUtils {

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveButtonTitle: String = "OK",
        negativeButtonTitle: String? = null,
        onPositiveClick: (() -> Unit)? = null,
        onNegativeClick: (() -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)

        // Set Positive Button
        builder.setPositiveButton(positiveButtonTitle) { dialog, _ ->
            dialog.dismiss()
            onPositiveClick?.invoke()
        }

        // Set Negative Button if provided
        if (!negativeButtonTitle.isNullOrEmpty()) {
            builder.setNegativeButton(negativeButtonTitle) { dialog, _ ->
                dialog.dismiss()
                onNegativeClick?.invoke()
            }
        }

        builder.create().show()
    }
}
