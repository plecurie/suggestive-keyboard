package com.example.suggestivekeyboardapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.BufferedReader
import java.io.FileReader
import java.io.InputStream
import java.net.URL
import java.util.concurrent.Executors


class SQLiteDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE table $TABLE_NAME (PREVIOUS TEXT, CURRENT TEXT, COUNT INTEGER) ");
        getRecommandations()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun getAllData(): Cursor {
        val db: SQLiteDatabase = this.writableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    private fun getRecommandations() {
        Executors.newSingleThreadExecutor().execute {
            val count : Int = URL("https://cdn.backinapp.com/test/spark/2Gram.txt").readText().toInt()
            for (i in 1..count) {
                val inputStream: InputStream = URL("https://cdn.backinapp.com/test/spark/2Gram.csv/${i}.csv").openStream()
                val reader = inputStream.bufferedReader()
                for (line in reader.lines()) {
                    if (!line.contains("PREVIOUS", ignoreCase = true)) {
                        val data = line.split(',')
                        if (data.size == 3)
                            this.addData(data[0], data[1], data[2])
                    }
                }
            }

            val count2 : Int = URL("https://cdn.backinapp.com/test/spark/3Gram.txt").readText().toInt()
            for (i in 1..count2) {
                val inputStream: InputStream = URL("https://cdn.backinapp.com/test/spark/3Gram.csv/${i}.csv").openStream()
                val reader = inputStream.bufferedReader()
                for (line in reader.lines()) {
                    if (!line.contains("PREVIOUS", ignoreCase = true)) {
                        val data = line.split(',')
                        if (data.size == 3)
                            this.addData(data[0], data[1], data[2])
                    }
                }
            }
        }
    }

    private fun addData(previous: String, current: String, count: String) : Boolean {
        val db : SQLiteDatabase = this.writableDatabase
        val contenValues : ContentValues = ContentValues()
        contenValues.put(COL_1, previous)
        contenValues.put(COL_2, current)
        contenValues.put(COL_3, count)
        val result: Long = db.insert(TABLE_NAME, null, contenValues)
        return !result.equals(-1)
    }

    companion object {
        const val DATABASE_NAME = "sample.db"
        const val TABLE_NAME = "sample_table"
        const val COL_1 = "PREVIOUS"
        const val COL_2 = "CURRENT"
        const val COL_3 = "COUNT"
    }
}
