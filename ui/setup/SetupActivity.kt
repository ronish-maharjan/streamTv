package com.streamtv.app.ui.setup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.streamtv.app.data.prefs.AppPrefs
import com.streamtv.app.databinding.ActivitySetupBinding
import com.streamtv.app.ui.home.HomeActivity

class SetupActivity : FragmentActivity() {

    private lateinit var binding: ActivitySetupBinding
    private lateinit var prefs: AppPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = AppPrefs(this)

        // Pre-fill if already saved
        if (prefs.serverIp.isNotEmpty()) {
            binding.etServerIp.setText(prefs.serverIp)
            binding.etApiKey.setText(prefs.apiKey)
        }

        binding.btnConnect.setOnClickListener {
            val ip = binding.etServerIp.text.toString().trim()
            val apiKey = binding.etApiKey.text.toString().trim()

            if (ip.isEmpty()) {
                Toast.makeText(this, "Please enter server IP", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.serverIp = ip
            prefs.apiKey = apiKey

            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
