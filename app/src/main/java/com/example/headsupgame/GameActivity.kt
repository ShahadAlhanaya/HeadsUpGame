package com.example.headsupgame

import android.app.Dialog
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.Window
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.random.Random
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import java.lang.reflect.Type


class GameActivity : AppCompatActivity() {

    lateinit var timerLandscapeTextView: TextView
    lateinit var timerPortraitTextView: TextView
    lateinit var celebrityNameTextView: TextView
    lateinit var taboo1TextView: TextView
    lateinit var taboo2TextView: TextView
    lateinit var taboo3TextView: TextView
    lateinit var landscapeLinearLayout: LinearLayout
    lateinit var portraitLinearLayout: LinearLayout
    lateinit var gameActivityLinearLayout: ConstraintLayout


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
        //only light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)//keep the app always on light mode

        //hide the action bar
        supportActionBar?.hide()

        val gson = Gson()
        val type: Type = object : TypeToken<List<Celebrities.Celebrity?>?>() {}.type
        val celebrities: List<Celebrities.Celebrity> = gson.fromJson<List<Celebrities.Celebrity>>(
            intent.getStringExtra("celebritiesList"),
            type
        )
        for (celebrity in celebrities) {
            celebritiesList.add(celebrity)
        }

        gameActivityLinearLayout = findViewById(R.id.cl_gameActivity)
        landscapeLinearLayout = findViewById(R.id.ll_gameLandScape)
        portraitLinearLayout = findViewById(R.id.ll_gamePortrait)
        timerLandscapeTextView = findViewById<TextView>(R.id.tv_timerLandscape)
        timerPortraitTextView = findViewById<TextView>(R.id.tv_timerPortrait)
        celebrityNameTextView = findViewById<TextView>(R.id.tv_celebrityName)
        taboo1TextView = findViewById<TextView>(R.id.tv_taboo1)
        taboo2TextView = findViewById<TextView>(R.id.tv_taboo2)
        taboo3TextView = findViewById<TextView>(R.id.tv_taboo3)

        setUI()
        val maxVolume = 100.0f
        val currentVolume = 40.0f
        val bgMusicMediaPlayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.bg_music)
        bgMusicMediaPlayer.setVolume(currentVolume/maxVolume,currentVolume/maxVolume)
        bgMusicMediaPlayer.start()
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
            if (!startGameFlag) {
                startGameFlag = true
                round++
                startGame()
            }
            gameActivityLinearLayout.setBackgroundResource(R.drawable.bg_landscape_game_activity)
            ll_gameLandScape.isVisible = true
            ll_gamePortrait.isVisible = false
            nextCelebrity()
        } else if (orientation === Configuration.ORIENTATION_PORTRAIT) {
            gameActivityLinearLayout.setBackgroundResource(R.drawable.bg_portrait_game_activity)
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
        val tickSoundMediaPlayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.human_tick)
        val winSoundMediaPlayer: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.win)
        object : CountDownTimer(60000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                timerLandscapeTextView.text = "" + millisUntilFinished / 1000
                timerPortraitTextView.text = "" + millisUntilFinished / 1000
                tickSoundMediaPlayer.start()
            }

            override fun onFinish() {
                showWinDialog()
                setUI()
                winSoundMediaPlayer.start()
            }
        }.start()
    }

    private fun showWinDialog() {

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.game_over_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val scoreTextView = dialog.findViewById<TextView>(R.id.tv_scoreNumber)
        val starsImageView = dialog.findViewById<ImageView>(R.id.stars)
        val returnButton = dialog.findViewById<ImageButton>(R.id.btn_return)
        val playAgainButton = dialog.findViewById<ImageButton>(R.id.btn_playAgain)
        val score = (round-1)  * 10
        scoreTextView.text = score.toString()
        when {
            score >= 50 -> {
                starsImageView.setImageDrawable(getDrawable(R.drawable.three_star))
            }
            score >= 30 -> {
                starsImageView.setImageDrawable(getDrawable(R.drawable.two_star))
            }
            score == 0 -> {
                starsImageView.visibility = View.INVISIBLE
            }
            else -> {
                starsImageView.setImageDrawable(getDrawable(R.drawable.one_star))
            }
        }
        returnButton.setOnClickListener {
            finish()
        }
        playAgainButton.setOnClickListener {
            this.recreate()
        }
        dialog.show()
    }


}