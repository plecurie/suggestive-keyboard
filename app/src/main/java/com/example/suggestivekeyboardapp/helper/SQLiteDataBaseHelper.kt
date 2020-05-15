package com.example.suggestivekeyboardapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.BufferedReader
import java.io.FileReader


class SQLiteDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE table $TABLE_NAME (PREVIOUS TEXT, CURRENT TEXT, COUNT INTEGER) ")
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

    fun retrieveRecommandationData(db: SQLiteDatabase, fileName: String) {
        val file = FileReader(fileName)
        val buffer = BufferedReader(file)
        var line = ""
        val tableName = "TABLE_NAME"
        val columns = "PREVIOUS, CURRENT, COUNT"
        val str1 = "INSERT INTO $tableName ($columns) values("
        val str2 = ");"

        db.beginTransaction()
        while (buffer.readLine().also({ line = it }) != null) {
            val sb = StringBuilder(str1)
            val str = line.split(",").toTypedArray()
            sb.append("'" + str[0] + "',")
            sb.append(str[1] + "',")
            sb.append(str[2] + "',")
            sb.append(str[3] + "'")
            sb.append(str[4] + "'")
            sb.append(str2)
            db.execSQL(sb.toString())
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun addData(previous: String, current: String, count: String) : Boolean {
        val db : SQLiteDatabase = this.writableDatabase
        val contenValues : ContentValues = ContentValues()
        contenValues.put(COL_1, current)
        contenValues.put(COL_2, previous)
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
