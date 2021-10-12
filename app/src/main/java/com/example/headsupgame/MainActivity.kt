package com.example.headsupgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.gson.Gson




class MainActivity : AppCompatActivity() {

    var celebritiesList = arrayListOf<Celebrities.Celebrity>()
    lateinit var startButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton = findViewById<Button>(R.id.btn_start)
        startButton.isClickable = false
        startButton.setOnClickListener {
            val gson = Gson()
            val jsonCelebritiesList: String = gson.toJson(celebritiesList)
            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra("celebritiesList",jsonCelebritiesList)
            startActivity(intent)
        }
        CoroutineScope(Dispatchers.IO).launch {
            getCelebritiesList(onResult = {
                startButton.isClickable = true
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
                    onResult()
                }

                override fun onFailure(call: Call<List<Celebrities.Celebrity>>, t: Throwable) {
                    startButton.isClickable = false
                    Toast.makeText(this@MainActivity, "" + t.message, Toast.LENGTH_SHORT).show()
                    onResult()
                }
            })
        }
    }
}