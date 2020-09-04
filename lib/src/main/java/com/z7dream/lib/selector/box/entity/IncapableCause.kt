package com.z7dream.lib.selector.box.entity

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.z7dream.lib.selector.box.widget.IncapableDialog

class IncapableCause {
    private var mForm = 0
    private var mTitle: String? = null
    private var mMessage: String

    constructor(message: String) {
        mMessage = message
    }

    constructor(title: String?, message: String) {
        mTitle = title
        mMessage = message
    }

    constructor(form: Int, message: String) {
        mForm = form
        mMessage = message
    }

    constructor(form: Int, title: String?, message: String) {
        mForm = form
        mTitle = title
        mMessage = message
    }

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class Form
    companion object {
        const val TOAST = 0
        const val DIALOG = 1
        const val NONE = 2
        fun handleCause(
            context: Context,
            cause: IncapableCause?
        ) {
            if (cause != null) {
                when (cause.mForm) {
                    0 -> Toast.makeText(context, cause.mMessage, Toast.LENGTH_SHORT).show()
                    1 -> {
                        val incapableDialog =
                            IncapableDialog.newInstance(cause.mTitle, cause.mMessage)
                        incapableDialog.show(
                            (context as FragmentActivity).supportFragmentManager,
                            IncapableDialog::class.java.name
                        )
                    }
                    2 -> {
                    }
                    else -> Toast.makeText(context, cause.mMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}