package com.bieldev.dwe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PhotoActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var buttonTakePhoto: Button
    private lateinit var buttonUsePhoto: Button
    private lateinit var buttonRetakePhoto: Button
    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_CAMERA_PERMISSION = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)

        imageView = findViewById(R.id.photo_imageView)
        buttonTakePhoto = findViewById(R.id.capture_button)
        buttonUsePhoto = findViewById(R.id.use_this_button)
        buttonRetakePhoto = findViewById(R.id.take_another_button)

        buttonUsePhoto.visibility = View.GONE
        buttonRetakePhoto.visibility = View.GONE

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        } else {
            takePhoto()
        }

        buttonUsePhoto.setOnClickListener {
            Toast.makeText(this, "Photo selected!", Toast.LENGTH_SHORT).show()
            // Handle the logic to use the selected photo here
        }

        buttonRetakePhoto.setOnClickListener {
            imageView.setImageBitmap(null)
            buttonUsePhoto.visibility = View.GONE
            buttonRetakePhoto.visibility = View.GONE
            takePhoto()
        }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                takePhoto()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val extras = data?.extras
                    val imageBitmap = extras?.get("data") as Bitmap
                    imageView.setImageBitmap(imageBitmap)
                    buttonUsePhoto.visibility = View.VISIBLE
                    buttonRetakePhoto.visibility = View.VISIBLE
                    buttonTakePhoto.visibility = View.GONE

                    // Iniciar PtQuestionActivity após a foto ser tirada
                    val intent = Intent(this, PtQuestionActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}