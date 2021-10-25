package com.example.headsupgame.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.headsupgame.Celebrities

class DBHelper (context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "celebrities.db"
        private const val TABLE_CELEBRITIES = "Celebrities"

        private const val KEY_PRIMARY_KEY = "pk"
        private const val KEY_NAME = "name"
        private const val KEY_TABOO1 = "taboo1"
        private const val KEY_TABOO2 = "taboo2"
        private const val KEY_TABOO3 = "taboo3"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE $TABLE_CELEBRITIES ($KEY_PRIMARY_KEY INTEGER PRIMARY KEY ,$KEY_NAME TEXT ,$KEY_TABOO1 TEXT ,$KEY_TABOO2 TEXT ,$KEY_TABOO3 TEXT)")
    }

    fun addCelebrity(name: String, taboo1: String, taboo2: String, taboo3: String): Long{
        val sqLiteDatabase = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, name)
        contentValues.put(KEY_TABOO1, taboo1)
        contentValues.put(KEY_TABOO2, taboo2)
        contentValues.put(KEY_TABOO3, taboo3)
        val success = sqLiteDatabase.insert(TABLE_CELEBRITIES,null, contentValues)
        sqLiteDatabase.close()
        return success
    }


    fun retrieveCelebrities(): ArrayList<Celebrities.Celebrity>{
        val sqLiteDatabase = writableDatabase
        var list = arrayListOf<Celebrities.Celebrity>()
        val cursor : Cursor = sqLiteDatabase.query(TABLE_CELEBRITIES, null,null,null,null,null,null)
        if (cursor.moveToFirst()) {
            do {
                val pk = cursor.getString(cursor.getColumnIndex(KEY_PRIMARY_KEY))
                val name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val taboo1 = cursor.getString(cursor.getColumnIndex(KEY_TABOO1))
                val taboo2 = cursor.getString(cursor.getColumnIndex(KEY_TABOO2))
                val taboo3 = cursor.getString(cursor.getColumnIndex(KEY_TABOO3))
                list.add(Celebrities.Celebrity(name,taboo1,taboo2,taboo3))
            } while (cursor.moveToNext());
        }
//        sqLiteDatabase.close()
        return list
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_CELEBRITIES")
        onCreate(db)
    }
}