package com.bieldev.dwe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.os.Environment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class PhotoActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var buttonTakePhoto: Button
    private lateinit var buttonUsePhoto: Button
    private lateinit var buttonRetakePhoto: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentPhotoPath: String = ""
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_LOCATION_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        buttonTakePhoto.setOnClickListener { checkPermissions() }
        buttonUsePhoto.setOnClickListener { navigateToPtQuestionActivity() }
        buttonRetakePhoto.setOnClickListener { takePhoto() }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CAMERA_PERMISSION)
        } else {
            takePhoto()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK)
            putExtra("android.intent.extras.LENS_FACING_BACK", 1)
            putExtra("android.intent.extra.USE_FRONT_CAMERA", false)
        }
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                takePhoto()
            } else {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
            getLocationAndSavePhoto(imageBitmap)
        }
    }

    private fun getLocationAndSavePhoto(imageBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val locationString = "Lat_${location.latitude}_Lon_${location.longitude}"
                    savePhotoWithLocation(imageBitmap, locationString)
                }
            }
        }
    }

    private fun savePhotoWithLocation(imageBitmap: Bitmap, locationString: String) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${locationString}_$timeStamp.jpg"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        storageDir?.let {
            val imageFile = File.createTempFile(imageFileName, ".jpg", it).apply {
                currentPhotoPath = absolutePath
            }
            FileOutputStream(imageFile).use { out ->
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                uploadFileToServer(currentPhotoPath)
            }
        } ?: run {
            Toast.makeText(this, "Failed to access storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFileToServer(filePath: String) {
        val client = OkHttpClient()
        val file = File(filePath)
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", file.name, requestBody)
            .build()

        val request = Request.Builder()
            .url("https://api.bielsv.net/upload")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@PhotoActivity, "Photo uploaded successfully", Toast.LENGTH_SHORT).show()
                        navigateToPtQuestionActivity()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@PhotoActivity, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun navigateToPtQuestionActivity() {
        val intent = Intent(this, PtQuestionActivity::class.java).apply {
            putExtra("firstPhotoPath", currentPhotoPath)
            putExtra("location", "Lat_Lon") // Replace with actual location if needed
        }
        startActivity(intent)
    }
}