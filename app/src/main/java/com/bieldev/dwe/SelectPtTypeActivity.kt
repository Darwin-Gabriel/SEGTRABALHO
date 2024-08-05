package com.bieldev.dwe

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class SelectPtTypeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_pt_type)

        val spinnerPtType = findViewById<Spinner>(R.id.spinner_pt_type)

        // Cria um ArrayAdapter usando o array de strings e o layout padrão do spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.pt_type_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Especifica o layout a ser usado quando a lista de opções aparece
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Aplica o adapter ao spinner
            spinnerPtType.adapter = adapter
        }
    }
}