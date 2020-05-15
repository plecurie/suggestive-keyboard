package com.example.suggestivekeyboardapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.example.suggestivekeyboardapp.helper.SQLiteDataBaseHelper


class MainActivity : AppCompatActivity() {

    var dbHelper : SQLiteDataBaseHelper = SQLiteDataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //viewAll()

        val editText = findViewById<EditText>(R.id.edit_area)
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                val db = dbHelper.writableDatabase

                val arrStr = s.toString().split(' ');
                if (arrStr.size == 2) {
                    val selectQuery1 = "SELECT * FROM sample_table WHERE PREVIOUS = ? and CURRENT = ?"
                    db.rawQuery(selectQuery1, arrayOf(arrStr[0], arrStr[1])).use {
                        if (it.moveToNext()) {
                            val count1 = it.getInt(2) + 1
                            val insertQuery = "UPDATE sample_table SET count = $count1 WHERE PREVIOUS = ? and CURRENT = ?"
                            db.rawQuery(insertQuery, arrayOf(arrStr[0], arrStr[1]))
                        }
                    }
                }

                val selectQuery = "SELECT * FROM sample_table WHERE PREVIOUS = ? ORDER BY count DESC LIMIT 3"
                db.rawQuery(selectQuery, arrayOf(s.toString())).use {
                    while (it.moveToNext()) {
                        println(it.getString(1))
                        println(it.getInt(2))
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
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