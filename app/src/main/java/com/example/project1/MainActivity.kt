package com.example.project1

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AdapterView.OnItemClickListener

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // creates Dropdown menu
        val firstPageSpinner = findViewById<Spinner>(R.id.spinner)
        ArrayAdapter.createFromResource(
            this, R.array.gestures_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            firstPageSpinner.adapter = adapter
        }

        // adds a listener to the dropdown menu, first sets so doesnt switched automaticall
        firstPageSpinner.setSelection(0, false)
        firstPageSpinner.onItemSelectedListener = object : OnItemClickListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                val text = firstPageSpinner.selectedItem.toString()
                intent.putExtra("gesture", text)
                startActivity(intent)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        // start download of service
    }
}

// references
// https://medium.com/android-news/kotlin-series-share-data-between-activities-using-explicit-intent-5a963cceaceb
// https://developer.android.com/docs