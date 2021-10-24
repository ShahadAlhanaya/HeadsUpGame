package com.example.headsupgame.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "celebrities.db"
        private const val TABLE_NOTES = "Celebrities"

        private const val KEY_PRIMARY_KEY = "pk"
        private const val KEY_NAME = "name"
        private const val KEY_TABOO1 = "taboo1"
        private const val KEY_TABOO2 = "taboo2"
        private const val KEY_TABOO3 = "taboo3"
    }

    var sqLiteDatabase : SQLiteDatabase = writableDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_NOTES ($KEY_PRIMARY_KEY INTEGER PRIMARY KEY ,$KEY_NAME TEXT ,$KEY_TABOO1 TEXT ,$KEY_TABOO2 TEXT ,$KEY_TABOO3 TEXT)")
    }

    fun addCelebrity(name: String, taboo1: String, taboo2: String, taboo3: String): Long{
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_TABOO1, taboo1)
        contentValues.put(KEY_TABOO2, taboo2)
        contentValues.put(KEY_TABOO3, taboo3)
        val success = sqLiteDatabase.insert(TABLE_NOTES,null, contentValues)
        sqLiteDatabase.close()
        return success
    }

    //we're not gonna implement it yet
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}
}