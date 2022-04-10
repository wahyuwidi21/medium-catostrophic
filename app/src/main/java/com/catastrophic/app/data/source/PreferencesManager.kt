package com.catastrophic.app.data.source

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesManager @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("cat-remote-key",Context.MODE_PRIVATE)
    private var editor = sharedPreferences.edit()

    var prefRemoteKey: Int?
        get() {
            return sharedPreferences.getInt("REMOTE_KEY", 1)
        }
        set(value) {
            if (value != null) {
                editor.putInt("REMOTE_KEY", value)?.apply()
            }
        }
}