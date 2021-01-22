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
    lateinit var coutdownDisplay:TextView

    lateinit var btn30min:Button
    lateinit var btn60min:Button
    lateinit var btn90min:Button
    lateinit var btn120min:Button

    //Default value in ms for countdown
    var timeToCountDownInMs = 5000L
    val timeTicks = 1000L

    // Is false when the counter is not counting
    var isCounting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coutdownDisplay = findViewById<TextView>(R.id.countDownView)

        btn30min = findViewById(R.id.btn30)
        btn30min.setOnClickListener() {
            updateCount(3000L)
        }

        btn60min = findViewById(R.id.btn60)
        btn60min.setOnClickListener() {
            updateCount(6000L)
        }

        btn90min = findViewById(R.id.btn90)
        btn90min.setOnClickListener() {
            updateCount(9000L)
        }

        btn120min = findViewById(R.id.btn120)
        btn120min.setOnClickListener() {
            updateCount(12000L)
        }

        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            if (!isCounting) {
                startCountDown(it)
            }
            startButton.isEnabled = false
            isCounting = true
        }
    }

    fun updateCount(ms:Long){
        timeToCountDownInMs = ms
        if(!isCounting) {
            updateCountDownDisplay(ms)
        }

    }

    fun startCountDown(v: View){
        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                startButton.isEnabled = true
                isCounting = false
            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }



        timer.start()
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}