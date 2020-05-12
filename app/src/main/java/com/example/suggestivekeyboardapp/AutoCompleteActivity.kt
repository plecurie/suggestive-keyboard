package com.example.suggestivekeyboardapp

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView

class AutoCompleteActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_autocomplete)

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_dropdown_item_1line, COUNTRIES
        )

        val textView = findViewById<AutoCompleteTextView>(R.id.autocomplete_textview)
        val countries: Array<out String> = resources.getStringArray(R.array.countries_array)

        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries).also { adapter ->
            textView.setAdapter(adapter)
        }
    }

    private val COUNTRIES = arrayOf(
        "Belgium", "France", "Italy", "Germany", "Spain"
    )

    fun Back(v: View?) {
        finish()
    }

}
