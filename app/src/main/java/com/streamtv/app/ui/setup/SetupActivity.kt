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

        // ✅ Pre-fill current values so user can see and edit them
        if (prefs.serverIp.isNotEmpty()) {
            binding.etServerIp.setText(prefs.serverIp)
            binding.etApiKey.setText(prefs.apiKey)

            // ✅ Change button text if already configured
            binding.btnConnect.text = "Save & Reconnect"

            // ✅ Show back button if already configured (came from settings)
            binding.btnBack.visibility = android.view.View.VISIBLE
        }

        binding.btnConnect.setOnClickListener {
            val ip = binding.etServerIp.text.toString().trim()
            val apiKey = binding.etApiKey.text.toString().trim()

            if (ip.isEmpty()) {
                Toast.makeText(this, "Please enter server IP and port", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Save new values
            prefs.serverIp = ip
            prefs.apiKey = apiKey

            Toast.makeText(this, "✅ Settings saved!", Toast.LENGTH_SHORT).show()

            // ✅ Go to home and clear back stack
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        // ✅ Back button — just close settings and go back
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}
