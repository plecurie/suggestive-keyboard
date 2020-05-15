package com.example.suggestivekeyboardapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.suggestivekeyboardapp.helper.SQLiteDataBaseHelper
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {

    var dbHelper : SQLiteDataBaseHelper = SQLiteDataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRecommandations();
        viewAll()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun getRecommandations() {
        Executors.newSingleThreadExecutor().execute {
            val count : Int = URL("https://cdn.backinapp.com/test/output/2Gram.txt").readText().toInt()
            for (i in 1..count) {
                val inputStream: InputStream = URL("https://cdn.backinapp.com/test/output/2Gram.csv/${i}.csv").openStream()
                val reader = inputStream.bufferedReader()
                for (line in reader.lines()) {
                    if (!line.contains("PREVIOUS", ignoreCase = true)) {
                        val data = line.split(',')
                        dbHelper.addData(data[0], data[1], data[2])
                    }
                }
            }
        }
    }

    private fun insertData(previous: String, current: String, rank: String) {
        val isInserted = dbHelper.addData(previous,current,rank)
        if (isInserted)
            Toast.makeText(this, "Data Inserted Successfully", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(this, "Data Not Inserted", Toast.LENGTH_LONG).show()
    }

    private fun viewAll() {
        val buffer : StringBuffer = StringBuffer()
        val data = dbHelper.getAllData()
        while (data.moveToNext()) {
            buffer.append("Previous : "+ data.getString(0)+"\n")
            buffer.append("Current : "+ data.getString(1)+"\n")
            buffer.append("Rank : "+ data.getString(2)+"\n")
        }
        showMessage("Data", buffer.toString())
    }

    private fun showMessage(title: String, message: String ) {
        val builder : AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setCancelable(true)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.show()
    }
}