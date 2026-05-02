package com.streamtv.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.ui.home.HomeActivity
import com.streamtv.app.ui.setup.SetupActivity

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = AppPrefs(this)

        // If server IP is already saved, go directly to Home
        if (prefs.isConfigured()) {
            startActivity(Intent(this, HomeActivity::class.java))
        } else {
            startActivity(Intent(this, SetupActivity::class.java))
        }
        finish()
    }
}
