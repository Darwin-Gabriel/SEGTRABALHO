// RiskActivity.kt
package com.bieldev.dwe

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class RiskActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        val workNumbers = arrayOf("141 - CONSÓRCIO MEDEIROS DARWIN\n", "Obra 2", "Obra 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, workNumbers)
        val editTextWorkNumber = findViewById<AutoCompleteTextView>(R.id.edit_text_work_number)
        editTextWorkNumber.setAdapter(adapter)

        val buttonNext = findViewById<Button>(R.id.button_next)
        buttonNext.setOnClickListener {
            val intent = Intent(this, QuestionActivity::class.java)
            startActivity(intent)
        }
    }
}