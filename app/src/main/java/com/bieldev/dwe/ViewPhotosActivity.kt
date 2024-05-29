package com.bieldev.dwe

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.IOException

class ViewPhotosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_photos)

        val photoStep1: ImageView = findViewById(R.id.photo_step1)
        val photoStep2: ImageView = findViewById(R.id.photo_step2)
        val photoStep3: ImageView = findViewById(R.id.photo_step3)
        val photoStep4: ImageView = findViewById(R.id.photo_step4)

        photoStep1.setImageURI(PhotoStorage.photoStep1)
        photoStep2.setImageURI(PhotoStorage.photoStep2)
        photoStep3.setImageURI(PhotoStorage.photoStep3)
        photoStep4.setImageURI(PhotoStorage.photoStep4)

        photoStep1.setOnClickListener { openFullscreenPhoto(PhotoStorage.photoStep1) }
        photoStep2.setOnClickListener { openFullscreenPhoto(PhotoStorage.photoStep2) }
        photoStep3.setOnClickListener { openFullscreenPhoto(PhotoStorage.photoStep3) }
        photoStep4.setOnClickListener { openFullscreenPhoto(PhotoStorage.photoStep4) }

        val sendPhotosButton: Button = findViewById(R.id.button_send_photos)
        sendPhotosButton.setOnClickListener { sendPhotosSequentially(listOf(PhotoStorage.photoStep1, PhotoStorage.photoStep2, PhotoStorage.photoStep3, PhotoStorage.photoStep4)) }
    }

    private fun openFullscreenPhoto(uri: Uri?) {
        val intent = Intent(this, FullscreenPhotoActivity::class.java)
        intent.putExtra("PHOTO_URI", uri.toString())
        startActivity(intent)
    }
    private fun sendPhotosSequentially(photoUris: List<Uri?>) {
        if (photoUris.isEmpty()) {
            Toast.makeText(this, "All photos sent", Toast.LENGTH_SHORT).show()
            return
        }

        val photoUri = photoUris[0]
        if (photoUri != null) {
            sendPhoto(photoUri) {
                sendPhotosSequentially(photoUris.drop(1))
            }
        } else {
            sendPhotosSequentially(photoUris.drop(1))
        }
    }

    private fun sendPhoto(photoUri: Uri, callback: () -> Unit) {
        val client = OkHttpClient()
        val file = File(photoUri.path!!)
        if (!file.exists() || !file.canRead()) {
            Log.e("ViewPhotosActivity", "File does not exist or cannot be read: ${file.path}")
            return
        }

        val fileBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, fileBody)
            .build()

        val request = Request.Builder()
            .url("https://api.bielsv.net/upload")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ViewPhotosActivity", "Failed to send photo: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@ViewPhotosActivity, "Erro ao enviar foto", Toast.LENGTH_SHORT).show()
                }
                callback()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("ViewPhotosActivity", "Failed to send photo: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@ViewPhotosActivity, "Erro ao enviar foto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("ViewPhotosActivity", "Photo sent successfully: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@ViewPhotosActivity, "Foto enviada com sucesso", Toast.LENGTH_SHORT).show()
                        if (response.code == 200) {
                            file.delete()  // Delete the file only if the response is successful
                        }
                    }
                }
                callback()
            }
        })
    }
}
