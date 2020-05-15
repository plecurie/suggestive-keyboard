package com.example.suggestivekeyboardapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.suggestivekeyboardapp.helper.SQLiteDataBaseHelper
import com.google.android.material.snackbar.Snackbar

open class MainActivity : AppCompatActivity() {

    var dbHelper : SQLiteDataBaseHelper = SQLiteDataBaseHelper(this)
    var duration  = 8000000

//    val intente = Intent(this, MapsActivity::class.java)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var message = findViewById<EditText>(R.id.edit_area)

        // insertData("previous", "current", "rank")

        viewAll()
        var btn = findViewById<Button>(R.id.btnval)
        btn.setOnClickListener {  popResto(message.text.toString()) }

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


    private fun popResto(msg: String) {
        var snackMessage = "voir les restaurants pour " + " " + msg + " ?"

        val mySnackbar = Snackbar.make(findViewById(R.id.edit_area), snackMessage, duration)
        Log.i("toto2","to2to")

        mySnackbar.setAction("afficher", MyrestoListener())





        if (msg == "poulet" || msg =="cheval"){ //    TODO ici changer la variuable par la list de la detection de bouf
            mySnackbar.show()
            Log.i("toto","victory")

        }

    }
    fun changeview(){
        Log.i("toto2","lunchactmap")

       // startActivity(intente)
    }
    private fun concol() {
        Log.i("toto2","relunch main ")

    }
}

