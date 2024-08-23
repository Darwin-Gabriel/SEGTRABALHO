package com.bieldev.dwe

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.util.UnstableApi
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

class RiskActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<String>
    private val client = OkHttpClient()
    private lateinit var savedData: String

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_risk)

        val editTextWorkNumber = findViewById<AutoCompleteTextView>(R.id.edit_text_work_number)
        val editTextStreetName = findViewById<EditText>(R.id.edit_text_street_name)
        val editTextManagerName = findViewById<EditText>(R.id.edit_text_supervisor_name)
        val buttonNext = findViewById<Button>(R.id.button_next)

        adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf())
        editTextWorkNumber.setAdapter(adapter)

        editTextWorkNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 1) {
                    fetchWorkNumbers(s.toString())
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        editTextWorkNumber.setOnItemClickListener { _, _, _, _ ->
            editTextWorkNumber.isFocusable = false
            editTextWorkNumber.isFocusableInTouchMode = false
            editTextWorkNumber.isFocusable = true
            editTextWorkNumber.isFocusableInTouchMode = true
        }

        editTextWorkNumber.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editTextWorkNumber.showDropDown()
            }
        }

        editTextWorkNumber.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                editTextWorkNumber.clearFocus()
                true
            } else {
                false
            }
        }

        buttonNext.setOnClickListener {
            val workNumber = editTextWorkNumber.text.toString()
            val streetName = editTextStreetName.text.toString()
            val managerName = editTextManagerName.text.toString()
            savedData = "NO=$workNumber&NR=$streetName&NE=$managerName"
            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("savedData", savedData)
            androidx.media3.common.util.Log.d("RiskActivity", "Navigating to QuestionActivity with data: $savedData")
            startActivity(intent)
        }
    }

    private fun fetchWorkNumbers(query: String) {
        val url = "https://api.bielsv.net/autocomplete?q=$query"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    response.body?.string()?.let { responseBody ->
                        val workNumbers = parseWorkNumbers(responseBody)
                        runOnUiThread {
                            adapter.clear()
                            adapter.addAll(workNumbers)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        })
    }

    private fun parseWorkNumbers(responseBody: String): List<String> {
        val jsonArray = JSONArray(responseBody)
        val workNumbers = mutableListOf<String>()
        for (i in 0 until jsonArray.length()) {
            workNumbers.add(jsonArray.getString(i))
        }
        return workNumbers
    }
}