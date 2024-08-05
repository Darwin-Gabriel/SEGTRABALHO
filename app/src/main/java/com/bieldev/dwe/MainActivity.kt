package com.bieldev.dwe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button_step1).setOnClickListener { startPhotoCapture(1) }
        findViewById<Button>(R.id.button_step2).setOnClickListener { startPhotoCapture(2) }
        findViewById<Button>(R.id.button_step3).setOnClickListener { startPhotoCapture(3) }
        findViewById<Button>(R.id.button_step4).setOnClickListener { startPhotoCapture(4) }
        findViewById<Button>(R.id.button_view_photos).setOnClickListener { viewAllPhotos() }
        findViewById<Button>(R.id.button_risks).setOnClickListener {
            val intent = Intent(this, RiskActivity::class.java)
            startActivity(intent)
        }

        // Chame a função sendRequest aqui se quiser que ela seja executada ao criar a atividade
        sendRequest()
    }

    private fun startPhotoCapture(step: Int) {
        val intent = Intent(this, PhotoActivity::class.java)
        intent.putExtra("STEP", step)
        startActivity(intent)
    }

    private fun viewAllPhotos() {
        val intent = Intent(this, ViewPhotosActivity::class.java)
        startActivity(intent)
    }

    private fun sendRequest() {
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", "cabr")
            .addFormDataPart("password", "1")
            .build()

        val request = Request.Builder()
            .url("https://api.bielsv.net/") // Substitua com a URL do seu servidor
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    throw IOException("Unexpected code $response")
                }

                // Faça algo com a resposta
            }
        })
    }
}