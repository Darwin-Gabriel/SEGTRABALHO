package com.bieldev.dwe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class QuestionActivity : AppCompatActivity() {

    companion object {
        const val TAG = "QuestionActivity"
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_LOCATION_PERMISSION = 101
    private var currentPhotoPath: String = ""
    private var firstPhotoPath: String = ""
    private var locationString: String = ""
    private lateinit var savedData: String
    private var isNoButtonClicked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }

        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonNo = findViewById<Button>(R.id.button_no)

        savedData = intent.getStringExtra("savedData") ?: ""
        Log.d(TAG, "Received savedData: $savedData")

        buttonYes.setOnClickListener {
            isNoButtonClicked = false
            takePhoto()
        }
        buttonNo.setOnClickListener {
            isNoButtonClicked = true
            takePhoto()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI = getPhotoFileUri()
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhotoFileUri(): Uri {
        val timeStamp: Long = System.currentTimeMillis() / 1000
        val imageFileName = "JPEG_${timeStamp}.jpg"
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: throw IOException("External Storage not available")
        val imageFile = File(storageDir, imageFileName)
        currentPhotoPath = imageFile.absolutePath
        return FileProvider.getUriForFile(this, "com.bieldev.dwe.fileprovider", imageFile)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = BitmapFactory.decodeFile(currentPhotoPath)
            if (imageBitmap != null) {
                firstPhotoPath = currentPhotoPath
                getLocationAndSavePhoto(imageBitmap)
            } else {
                Log.e(TAG, "Failed to decode the image file")
            }
        } else {
            Log.e(TAG, "Failed to capture image or result code is not OK")
        }
    }

    private fun getLocationAndSavePhoto(imageBitmap: Bitmap) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    locationString = "${it.latitude},${it.longitude}"
                    savePhotoWithLocation(imageBitmap)
                }
            }
        }
    }

    private fun savePhotoWithLocation(imageBitmap: Bitmap) {
        val timeStamp: Long = System.currentTimeMillis() / 1000
        val imageFileName = "${locationString}_${timeStamp}.jpg"
        val storageDir: File = getExternalFilesDir(null) ?: throw IOException("External Storage not available")
        val imageFile = File(storageDir, imageFileName)
        FileOutputStream(imageFile).use { out ->
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        currentPhotoPath = imageFile.absolutePath
        if (firstPhotoPath.isEmpty()) {
            firstPhotoPath = currentPhotoPath
        }
        uploadFileToServer(currentPhotoPath)
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
                    Toast.makeText(this@QuestionActivity, "Failed to upload file", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@QuestionActivity, "File uploaded successfully", Toast.LENGTH_SHORT).show()
                        navigateToPtQuestionActivity()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@QuestionActivity, "Failed to upload file", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun navigateToPtQuestionActivity() {
        val intent = Intent(this, PtQuestionActivity::class.java)
        intent.putExtra("savedData", savedData)
        intent.putExtra("firstPhotoPath", firstPhotoPath)
        intent.putExtra("location", locationString)
        Log.d(TAG, "Navigating to PtQuestionActivity with data: $savedData, firstPhotoPath: $firstPhotoPath, location: $locationString")
        startActivity(intent)
    }
}