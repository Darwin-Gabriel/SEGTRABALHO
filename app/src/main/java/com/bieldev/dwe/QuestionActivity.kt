package com.bieldev.dwe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class QuestionActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_LOCATION_PERMISSION = 101
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }

        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonNo = findViewById<Button>(R.id.button_no)

        buttonYes.setOnClickListener { takePhoto() }
        buttonNo.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            getLocationAndSavePhoto(imageBitmap)
        }
    }

    private fun getLocationAndSavePhoto(imageBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val locationString = "Lat_${location.latitude}_Lon_${location.longitude}"
                    val photoPath = savePhotoWithLocation(imageBitmap, locationString)
                    uploadFileToServer(photoPath)
                } else {
                    fusedLocationClient.lastLocation.addOnFailureListener {
                        Toast.makeText(this, "Failed to get precise location, using approximate location", Toast.LENGTH_SHORT).show()
                        val photoPath = savePhotoWithLocation(imageBitmap, "ApproximateLocation")
                        uploadFileToServer(photoPath)
                    }
                }
            }
        }
    }

    private fun savePhotoWithLocation(imageBitmap: Bitmap, locationString: String): String {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${locationString}_$timeStamp.jpg"
        val storageDir: File = getExternalFilesDir(null) ?: throw IOException("External Storage not available")
        val imageFile = File.createTempFile(imageFileName, ".jpg", storageDir)
        FileOutputStream(imageFile).use { out ->
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        currentPhotoPath = imageFile.absolutePath
        return currentPhotoPath
    }

    private fun uploadFileToServer(filePath: String) {
        val client = OkHttpClient()
        val file = File(filePath)
        val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, requestBody)
            .build()

        val request = Request.Builder()
            .url("https://api.bielsv.net/upload")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@QuestionActivity, "Failed to upload photo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@QuestionActivity, "Photo uploaded successfully", Toast.LENGTH_SHORT).show()
                        navigateToPtQuestionActivity()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@QuestionActivity, "Failed to upload photo", Toast.LENGTH_SHORT).show()
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
//