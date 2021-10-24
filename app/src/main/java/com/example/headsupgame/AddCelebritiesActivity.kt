package com.example.headsupgame

import android.database.sqlite.SQLiteOpenHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.headsupgame.database.DBHelper

class AddCelebritiesActivity : AppCompatActivity() {

    lateinit var addButton: ImageButton
    lateinit var closeButton: ImageButton
    lateinit var nameEditText: EditText
    lateinit var taboo1EditText: EditText
    lateinit var taboo2EditText: EditText
    lateinit var taboo3EditText: EditText
    lateinit var databaseHelper: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_celebrities)
        //only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//keep the app always on light mode

        //hide the action bar
        supportActionBar?.hide()


        databaseHelper = DBHelper(applicationContext)

        addButton = findViewById(R.id.btn_addCelebrity)
        closeButton = findViewById(R.id.btn_close)
        nameEditText = findViewById(R.id.edt_celebrityName)
        taboo1EditText = findViewById(R.id.edt_celebrityTaboo1)
        taboo2EditText = findViewById(R.id.edt_celebrityTaboo2)
        taboo3EditText = findViewById(R.id.edt_celebrityTaboo3)

        closeButton.setOnClickListener { finish() }



        addButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val taboo1 = taboo1EditText.text.toString().trim()
            val taboo2 = taboo2EditText.text.toString().trim()
            val taboo3 = taboo3EditText.text.toString().trim()

            if(name.isNotEmpty() && taboo1.isNotEmpty() && taboo2.isNotEmpty() && taboo3.isNotEmpty()){

                if (databaseHelper.addCelebrity(name,taboo1,taboo2,taboo3) > 0) {
                    Toast.makeText(applicationContext, "Added successfully!", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT)
                        .show()
                }

            }else{
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}