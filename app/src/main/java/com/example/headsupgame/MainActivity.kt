package com.example.headsupgame

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_game.*
import android.media.AudioManager

import android.media.SoundPool
import android.media.MediaPlayer








class MainActivity : AppCompatActivity() {

    var celebritiesList = arrayListOf<Celebrities.Celebrity>()
    lateinit var startButton: ImageButton
    lateinit var mainActivityLinearLayout: ConstraintLayout
    lateinit var startButtonFrameLayout: FrameLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//keep the app always on light mode

        //hide the action bar
        supportActionBar?.hide()


        mainActivityLinearLayout = findViewById(R.id.cl_mainActivity)
        startButtonFrameLayout = findViewById(R.id.fl_startButton)
        setUI()
        startButton = findViewById(R.id.btn_start)
        startButtonFrameLayout.visibility = View.INVISIBLE
        startButton.setOnClickListener {
            val gson = Gson()
            val jsonCelebritiesList: String = gson.toJson(celebritiesList)
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("celebritiesList", jsonCelebritiesList)
            startActivity(intent)
        }
        CoroutineScope(Dispatchers.IO).launch {
            getCelebritiesList(onResult = {
            })
        }
    }

    private fun getCelebritiesList(onResult: () -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)
        if (apiInterface != null) {
            apiInterface.getCelebrities()?.enqueue(object : Callback<List<Celebrities.Celebrity>> {
                override fun onResponse(
                    call: Call<List<Celebrities.Celebrity>>,
                    response: Response<List<Celebrities.Celebrity>>
                ) {
                    Log.d("GET Response:", response.code().toString() + " " + response.message())
                    celebritiesList.clear()
                    for (Celebrity in response.body()!!) {
                        celebritiesList.add(Celebrity)
                    }
                    startButtonFrameLayout.visibility = View.VISIBLE
                    onResult()
                }

                override fun onFailure(call: Call<List<Celebrities.Celebrity>>, t: Throwable) {
                    startButtonFrameLayout.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity, "" + t.message, Toast.LENGTH_SHORT).show()
                    onResult()
                }
            })
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUI()
    }

    private fun setUI() {
        val orientation = resources.configuration.orientation
        if (orientation === Configuration.ORIENTATION_LANDSCAPE) {
            mainActivityLinearLayout.setBackgroundResource(R.drawable.bg_landscape_main_acitvity)
        } else if (orientation === Configuration.ORIENTATION_PORTRAIT) {
            mainActivityLinearLayout.setBackgroundResource(R.drawable.bg_portrait_main_acitvity)
        }
    }
}