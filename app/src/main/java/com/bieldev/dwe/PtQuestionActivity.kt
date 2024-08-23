package com.bieldev.dwe

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PtQuestionActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val TAG = "PtQuestionActivity"
    }

    private lateinit var savedData: String
    private lateinit var firstPhotoPath: String
    private lateinit var secondPhotoPath: String
    private lateinit var location: String
    private var protocol: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_question)

        val buttonNo = findViewById<Button>(R.id.button_no)
        val buttonYes = findViewById<Button>(R.id.button_yes)

        savedData = intent.getStringExtra("savedData") ?: ""
        firstPhotoPath = intent.getStringExtra("firstPhotoPath") ?: ""
        location = intent.getStringExtra("location") ?: ""

        buttonNo.setOnClickListener {
            takePhoto()
        }

        buttonYes.setOnClickListener {
            takePhoto()
        }

        Log.d(TAG, "Received firstPhotoPath: $firstPhotoPath")
        Log.d(TAG, "Received location: $location")

        fetchProtocol()
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                secondPhotoPath = savePhoto(imageBitmap, location)
                protocol?.let {
                    sendDataToServer(savedData, firstPhotoPath, secondPhotoPath, it)
                }
            } else {
                Log.e(TAG, "Failed to capture image or result code is not OK")
            }
        }
    }

    private fun savePhoto(imageBitmap: Bitmap, location: String): String {
        val timeStamp: Long = System.currentTimeMillis() / 1000
        val imageFileName = "${location}_${timeStamp}.jpg"
        val storageDir: File = getExternalFilesDir(null) ?: throw IOException("External Storage not available")
        val imageFile = File(storageDir, imageFileName)
        FileOutputStream(imageFile).use { out ->
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        Log.d(TAG, "Photo saved at: ${imageFile.absolutePath}")
        return imageFile.absolutePath
    }

    private fun fetchProtocol() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://api.bielsv.net/protocolo")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@PtQuestionActivity, "Failed to fetch protocol", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        protocol = responseBody
                        Log.d(TAG, "Fetched protocol: $protocol")
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@PtQuestionActivity, "Failed to fetch protocol", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun sendDataToServer(data: String, firstPhotoPath: String, secondPhotoPath: String, protocol: String) {
        val client = OkHttpClient()
        val no = data.split("&")[0].split("=")[1]
        val nr = data.split("&")[1].split("=")[1]
        val ne = data.split("&")[2].split("=")[1]

        Log.d(TAG, "Sending data with PRT: $protocol, NO: $no, NR: $nr, NE: $ne")
        val f1: String = File(firstPhotoPath).name
        val f2: String = File(secondPhotoPath).name
        val protocol2: String = protocol.toString()
        val no2: String = no.toString()
        val nr2: String = nr.toString()
        val ne2: String = ne.toString()
        val url = "https://api.bielsv.net/sst"
        val formBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("F1", f1)
            .addFormDataPart("F2", f2)
            .addFormDataPart("PRT", protocol2)
            .addFormDataPart("NO", no2)
            .addFormDataPart("NR", nr2)
            .addFormDataPart("NE", ne2)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@PtQuestionActivity, "Failed to send data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "Response code: ${response.code}")
                response.body?.string()?.let { responseBody ->
                    Log.d(TAG, "Response body: $responseBody")
                }
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@PtQuestionActivity, "Data sent successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@PtQuestionActivity, "Failed to send data2", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}