package com.catastrophic.app.utils

import android.app.Activity
import android.view.WindowManager


fun hideKeyboard(view: Activity) {
    view.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}