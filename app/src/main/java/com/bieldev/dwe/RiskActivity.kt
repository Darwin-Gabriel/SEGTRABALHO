package com.bieldev.dwe

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

// RiskActivity.kt
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