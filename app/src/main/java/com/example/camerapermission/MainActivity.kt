package com.example.camerapermission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import coil.load
import com.example.camerapermission.databinding.ActivityMainBinding
import java.io.File

class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 1


    @RequiresApi(Build.VERSION_CODES.M)
    val permissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            when {
                granted -> {
                    pickImage()
                }
                !granted -> {
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                        Toast.makeText(applicationContext, "Access Denied ", Toast.LENGTH_SHORT)
                            .show()
                        showPermissionAlert()
                    }
                }
            }
        }

    lateinit var binding: ActivityMainBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cameraButton.setOnClickListener {
            openCamera()
        }


    }

    private fun pickImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun openCamera() {
        permissionsLauncher.launch(Manifest.permission.CAMERA)

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroy() {
        permissionsLauncher.unregister()
        super.onDestroy()
    }

    private fun showPermissionAlert() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", this.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            binding.image.load(bitmap)

        }
    }
}