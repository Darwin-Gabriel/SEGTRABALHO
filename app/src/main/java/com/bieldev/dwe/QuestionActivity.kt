// QuestionActivity.kt
package com.bieldev.dwe

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class QuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonNo = findViewById<Button>(R.id.button_no)

        buttonYes.setOnClickListener {
            // Ação quando o usuário clicar em "Sim"
        }

        buttonNo.setOnClickListener {
            // Ação quando o usuário clicar em "Não"
        }
    }
}