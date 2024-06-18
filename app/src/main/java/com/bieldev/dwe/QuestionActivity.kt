// QuestionActivity.kt
package com.bieldev.dwe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonNo = findViewById<Button>(R.id.button_no)

        val photoIntent = Intent(this, PhotoActivity::class.java)

        buttonYes.setOnClickListener {
            startActivity(photoIntent)
        }

        buttonNo.setOnClickListener {
            startActivity(photoIntent)
        }
    }
}