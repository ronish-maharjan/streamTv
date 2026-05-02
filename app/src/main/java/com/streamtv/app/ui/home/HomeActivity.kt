package com.streamtv.app.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.FragmentActivity
import com.streamtv.app.R
import com.streamtv.app.databinding.ActivityHomeBinding
import com.streamtv.app.ui.setup.SetupActivity

class HomeActivity : FragmentActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.home_fragment_container, HomeFragment())
                .commit()
        }
    }

    // Long press on MENU button → go to settings
    override fun onKeyLongPress(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            startActivity(Intent(this, SetupActivity::class.java))
            return true
        }
        return super.onKeyLongPress(keyCode, event)
    }
}
