package com.example.suggestivekeyboardapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.suggestivekeyboardapp.helper.SQLiteDataBaseHelper


class MainActivity : AppCompatActivity() {

    var dbHelper : SQLiteDataBaseHelper = SQLiteDataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewAll()
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
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