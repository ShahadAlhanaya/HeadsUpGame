package com.example.headsupgame

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.random.Random
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import java.lang.reflect.Type


class GameActivity : AppCompatActivity() {

    lateinit var timerTextView: TextView
    lateinit var celebrityNameTextView: TextView
    lateinit var taboo1TextView: TextView
    lateinit var taboo2TextView: TextView
    lateinit var taboo3TextView: TextView
    lateinit var landscapeLinearLayout: LinearLayout
    lateinit var portraitLinearLayout: LinearLayout


    var celebritiesList = arrayListOf<Celebrities.Celebrity>()
    var playerCelebritiesList = arrayListOf<Celebrities.Celebrity>()
    var round = 0

    var startGameFlag = false
//    private var startGameFlag by Delegates.observable(false) { _, oldValue, newValue ->
//        Log.d("HELP", "the value is: $newValue")
////        startGame()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        val gson = Gson()
        val type: Type = object : TypeToken<List<Celebrities.Celebrity?>?>() {}.type
        val celebrities: List<Celebrities.Celebrity> = gson.fromJson<List<Celebrities.Celebrity>>(intent.getStringExtra("celebritiesList"), type)
        for (celebrity in celebrities) {
            celebritiesList.add(celebrity)
        }

        landscapeLinearLayout = findViewById(R.id.ll_gameLandScape)
        portraitLinearLayout = findViewById(R.id.ll_gamePortrait)
        timerTextView = findViewById<TextView>(R.id.tv_timer)
        celebrityNameTextView = findViewById<TextView>(R.id.tv_celebrityName)
        taboo1TextView = findViewById<TextView>(R.id.tv_taboo1)
        taboo2TextView = findViewById<TextView>(R.id.tv_taboo2)
        taboo3TextView = findViewById<TextView>(R.id.tv_taboo3)

        setUI()


    }

    private fun startGame() {
            startTimer()
            playerCelebritiesList = randomCelebritiesList()
            celebrityNameTextView.text = playerCelebritiesList[round].name
            taboo1TextView.text = playerCelebritiesList[round].taboo1
            taboo2TextView.text = playerCelebritiesList[round].taboo2
            taboo3TextView.text = playerCelebritiesList[round].taboo3
    }

    private fun nextCelebrity() {
        if (celebritiesList.isNotEmpty()) {
            celebrityNameTextView.text = playerCelebritiesList[round].name
            taboo1TextView.text = playerCelebritiesList[round].taboo1
            taboo2TextView.text = playerCelebritiesList[round].taboo2
            taboo3TextView.text = playerCelebritiesList[round].taboo3
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setUI()
    }

    private fun setUI() {
        val orientation = resources.configuration.orientation
        if (orientation === Configuration.ORIENTATION_LANDSCAPE) {
            if(!startGameFlag){
                startGameFlag = true
                startGame()
            }
            ll_gameLandScape.isVisible = true
            ll_gamePortrait.isVisible = false
            nextCelebrity()
        } else if (orientation === Configuration.ORIENTATION_PORTRAIT) {
            ll_gameLandScape.isVisible = false
            ll_gamePortrait.isVisible = true
            round++
        }
    }

    private fun randomCelebritiesList(): ArrayList<Celebrities.Celebrity> {
        val randomCelebrities = arrayListOf<Celebrities.Celebrity>()
        var count = 0;
        while (count < celebritiesList.size) {
            val random = Random.nextInt(0, celebritiesList.size)
            if (!randomCelebrities.contains(celebritiesList[random])) {
                randomCelebrities.add(celebritiesList[random])
                count++
            } else {
                continue
            }
        }
        return randomCelebrities
    }


    private fun startTimer() {
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {
                showWinDialog()
            }
        }.start()
    }

    private fun showWinDialog(){
        val builder = AlertDialog.Builder(this@GameActivity)
        builder.setMessage("Are you sure you want to delete this entry?")
            .setCancelable(false)
            .setPositiveButton("Play Again") { _, _ ->
                this.recreate()
            }
            .setNegativeButton("Return") { _, _ ->
                finish()
            }
        val alert = builder.create()
        alert.show()
//        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(resources.getColor(R.color.purple_200));
    }


}