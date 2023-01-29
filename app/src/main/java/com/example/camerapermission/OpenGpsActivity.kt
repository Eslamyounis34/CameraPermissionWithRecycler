package com.example.camerapermission

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.camerapermission.databinding.ActivityOpenGpsBinding

class OpenGpsActivity : AppCompatActivity() {
    lateinit var binding: ActivityOpenGpsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOpenGpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gpsButton.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=30.061148616702244,31.280885265607736&mode=l"))
            intent.setPackage("com.google.android.apps.maps")

            startActivity(intent)

        }
    }
}