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
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import coil.load
import com.example.camerapermission.databinding.ActivityMainBinding
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : AppCompatActivity() {

    private val CAMERA_REQUEST_CODE = 1

    var imagesList = arrayListOf<Bitmap>()



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

        binding.convertBT.setOnClickListener {
           var partsList= bitmapListToMultipartList(imagesList)
            Log.e("TestPartsImages",partsList.toString())
        }

        binding.imagesRecycler.apply {
            adapter = ImagesAdapter(imagesList)
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
            imagesList.add(bitmap)


            Log.e("ImagesList", imagesList.toString())

            binding.imagesRecycler.apply {
                adapter = ImagesAdapter(imagesList)
            }
        }
    }

    fun bitmapListToMultipartList(bitmapList: List<Bitmap>): List<MultipartBody.Part> {
        val parts = mutableListOf<MultipartBody.Part>()
        for (i in bitmapList.indices) {
            val bitmap = bitmapList[i]
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val requestBody = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray())
            parts.add(MultipartBody.Part.createFormData("image$i", "image$i.jpg", requestBody))
        }
        return parts
    }
}