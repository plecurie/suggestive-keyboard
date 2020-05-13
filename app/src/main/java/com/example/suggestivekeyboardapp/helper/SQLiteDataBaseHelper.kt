package com.example.suggestivekeyboardapp.helper

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteDataBaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE table $TABLE_NAME (PREVIOUS TEXT, CURRENT TEXT, RANK INTEGER) ")
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

    fun addData(previous: String, current: String, rank: String) : Boolean {
        val db : SQLiteDatabase = this.writableDatabase
        val contenValues : ContentValues = ContentValues()
        contenValues.put(COL_1, current)
        contenValues.put(COL_2, previous)
        contenValues.put(COL_3, rank)
        val result: Long = db.insert(TABLE_NAME, null, contenValues)
        return !result.equals(-1)
    }

    companion object {
        const val DATABASE_NAME = "sample.db"
        const val TABLE_NAME = "sample_table"
        const val COL_1 = "PREVIOUS"
        const val COL_2 = "CURRENT"
        const val COL_3 = "RANK"
    }
}
