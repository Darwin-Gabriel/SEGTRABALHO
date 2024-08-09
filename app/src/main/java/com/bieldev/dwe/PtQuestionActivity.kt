package com.bieldev.dwe

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody

class PtQuestionActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
        const val TAG = "PtQuestionActivity"
    }

    private var protocolo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_question)

        val buttonNo = findViewById<Button>(R.id.button_no)
        val buttonYes = findViewById<Button>(R.id.button_yes)

        buttonNo.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Alerta")
                .setMessage("FAVOR PARAR A OBRA IMEDIATAMENTE")
                .setPositiveButton(android.R.string.ok) { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }
                .show()
        }

        buttonYes.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        // Log to check if intent extras are passed correctly
        val firstPhotoPath = intent.getStringExtra("firstPhotoPath")
        val location = intent.getStringExtra("location")
        Log.d(TAG, "Received firstPhotoPath: $firstPhotoPath")
        Log.d(TAG, "Received location: $location")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            if (imageBitmap != null) {
                val savedPhotoPath = savePhoto(imageBitmap, intent.getStringExtra("location") ?: "")
                uploadFilesToServer(listOf(savedPhotoPath))
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
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out) // Save with maximum quality JPEG compression
        }
        Log.d(TAG, "Photo saved at: ${imageFile.absolutePath}")
        return imageFile.absolutePath
    }

    private fun uploadFilesToServer(filePaths: List<String>) {
        val client = OkHttpClient()
        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

        filePaths.forEach { filePath ->
            val file = File(filePath)
            val requestBody = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            multipartBodyBuilder.addFormDataPart("file", file.name, requestBody)
        }

        val request = Request.Builder()
            .url("https://api.bielsv.net/upload")
            .post(multipartBodyBuilder.build())
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@PtQuestionActivity, "Failed to upload photos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@PtQuestionActivity, "Photos uploaded successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@PtQuestionActivity, "Failed to upload photos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}