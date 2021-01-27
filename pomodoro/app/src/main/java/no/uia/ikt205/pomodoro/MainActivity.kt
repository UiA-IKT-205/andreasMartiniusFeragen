package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var countdownDisplay:TextView

    //Default value in ms for countdown
    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Countdown text
        countdownDisplay = findViewById<TextView>(R.id.countDownView)

        // Countdown time buttons
        val setTimeDurationButtons = listOf<Button>(
                findViewById<Button>(R.id.btn30),
                findViewById<Button>(R.id.btn60),
                findViewById<Button>(R.id.btn90),
                findViewById<Button>(R.id.btn120)
        )

        setTimeDurationButtons.forEachIndexed { index, button ->
            button.setOnClickListener(){
                if(startButton.isEnabled) {
                    val newCountdownTime:Long = (index+1) * 3000L
                    updateCountDownTime(newCountdownTime)
                }
            }
        }

        // Start button
        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            if (startButton.isEnabled) {
                startCountDown(it)
            }
            startButton.isEnabled = false
        }
    }

    fun updateCountDownTime(ms:Long){
        timeToCountDownInMs = ms
        if(startButton.isEnabled) {
            updateCountDownDisplay(ms)
        }
    }

    fun startCountDown(v: View){
        timer = object : CountDownTimer(timeToCountDownInMs, timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                v.isEnabled = true
            }
            override fun onTick(millisUntilFinished: Long) {
                updateCountDownDisplay(millisUntilFinished)
            }
        }
        v.isEnabled = false
        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        countdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}