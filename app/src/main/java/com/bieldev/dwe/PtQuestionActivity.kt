// PtQuestionActivity.kt
package com.bieldev.dwe

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class PtQuestionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pt_question)

        // Atualizar o cabeçalho para exibir a pergunta "Tem PT ?"
        supportActionBar?.title = "Tem PT ?"

        val buttonYes = findViewById<Button>(R.id.button_yes)
        val buttonNo = findViewById<Button>(R.id.button_no)

        // Aqui você pode definir o que acontece quando o usuário clica em "SIM" ou "NÃO"
        buttonYes.setOnClickListener {
            // startActivity(Intent(this, NextActivity::class.java))
        }

        buttonNo.setOnClickListener {
            // startActivity(Intent(this, NextActivity::class.java))
        }
    }
}