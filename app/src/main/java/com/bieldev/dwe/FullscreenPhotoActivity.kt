package com.bieldev.dwe

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullscreenPhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_photo)

        val fullscreenImage: ImageView = findViewById(R.id.fullscreen_image)
        val closeButton: Button = findViewById(R.id.close_button)

        val photoUri = intent.getStringExtra("PHOTO_URI")
        fullscreenImage.setImageURI(Uri.parse(photoUri))

        closeButton.setOnClickListener {
            finish()
        }
    }
} //