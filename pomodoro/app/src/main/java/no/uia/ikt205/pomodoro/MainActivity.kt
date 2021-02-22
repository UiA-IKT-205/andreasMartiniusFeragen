package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime
import no.uia.ikt205.pomodoro.util.minutesToMilliseconds
import java.lang.NumberFormatException

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var activityCountDownDisplay:TextView
    lateinit var pauseCountDownDisplay:TextView

    lateinit var activityTimeSeekBar:SeekBar
    lateinit var pauseTimeSeekBar:SeekBar

    lateinit var numberOfRespTextEdit:TextInputEditText

    var numberOfReps = 1

    //Default value in ms for countdown
    var timeToCountDownForActivityInMs = 5000L
    var timeToCountDownInForPauseMs = 5000L
    val timeTicks = 1000L

    enum class StartBtnStatus (val value: String){
        Start ("Start"),
        Stop ("Stop"),
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        numberOfRespTextEdit = findViewById<TextInputEditText>(R.id.NumberOfRespTextEdit)


        activityCountDownDisplay = findViewById<TextView>(R.id.activityTimeText)
        pauseCountDownDisplay = findViewById<TextView>(R.id.pauseTimeText)


        activityTimeSeekBar = findViewById<SeekBar>(R.id.activityTimeSeekBar)
        activityTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateSeekBar(activityTimeSeekBar)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        updateSeekBar(activityTimeSeekBar)


        pauseTimeSeekBar = findViewById<SeekBar>(R.id.pauseTimeSeekBar)
        pauseTimeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateSeekBar(pauseTimeSeekBar)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        updateSeekBar(pauseTimeSeekBar)


        startButton = findViewById<Button>(R.id.startCountdownButton)
        startButton.setOnClickListener(){
            try {
                // .text doesn't like toInt() if it isn't first cast to a string or something
                numberOfReps = numberOfRespTextEdit.text.toString().toInt()

                if (numberOfReps < 1){
                    Toast.makeText(this@MainActivity, "You need at least 1 session", Toast.LENGTH_SHORT).show()
                } else {
                    // Checks if the timer should be started or reset/stopped
                    if (startButton.text == StartBtnStatus.Start.value) {
                        startActivityTimer()
                        startButton.text = StartBtnStatus.Stop.value
                        numberOfRespTextEdit.isEnabled = false
                    } else {
                        resetTimers()
                        startButton.text = StartBtnStatus.Start.value
                        numberOfRespTextEdit.isEnabled = true
                    }
                }

            } catch ( e: NumberFormatException) {
                Toast.makeText(this@MainActivity, "This isn't a number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun startActivityTimer(){
        timer = object : CountDownTimer(timeToCountDownForActivityInMs, timeTicks) {
            override fun onFinish() {
                activityTimeSeekBar.isEnabled = true

                updatesSessionsLeft(-1)
                if (numberOfReps >= 1) {
                    startPauseTimer()
                } else {
                    startButton.text = StartBtnStatus.Start.value
                    numberOfRespTextEdit.isEnabled = true
                }
                updateSeekBar(activityTimeSeekBar)
            }
            override fun onTick(millisUntilFinished: Long) {
                updateActivityCountDownDisplay(millisUntilFinished.toInt())
            }
        }
        timer.start()
        activityTimeSeekBar.isEnabled = false
    }

    fun startPauseTimer(){
        timer = object : CountDownTimer(timeToCountDownInForPauseMs, timeTicks) {
            override fun onFinish() {
                pauseTimeSeekBar.isEnabled = true
                updateSeekBar(pauseTimeSeekBar)
                startActivityTimer()
            }
            override fun onTick(millisUntilFinished: Long) {
                updatePauseCountDownDisplay(millisUntilFinished.toInt())
            }
        }
        timer.start()
        pauseTimeSeekBar.isEnabled = false
    }

    // Stops timer and resets seeksbar's associated text to the current progress value
    fun resetTimers() {
        timer.cancel()

        activityTimeSeekBar.isEnabled = true
        pauseTimeSeekBar.isEnabled = true

        updateSeekBar(activityTimeSeekBar)
        updateSeekBar(pauseTimeSeekBar)
    }

    // Updates seeksbar's associated text and time to the current progress value
    fun updateSeekBar(seekBarObject:SeekBar) {
        if(seekBarObject.id == R.id.activityTimeSeekBar) {
            updateActivityCountDownDisplay(minutesToMilliseconds(seekBarObject.progress))
            timeToCountDownForActivityInMs = minutesToMilliseconds(seekBarObject.progress).toLong()
        } else {
            updatePauseCountDownDisplay(minutesToMilliseconds(seekBarObject.progress))
            timeToCountDownInForPauseMs = minutesToMilliseconds(seekBarObject.progress).toLong()
        }
    }

    fun updateActivityCountDownDisplay(timeInMsec:Int) {
        val timeToDescriptiveTime = millisecondsToDescriptiveTime(timeInMsec)
        val activityTimeString = "Activity Time : $timeToDescriptiveTime"
        activityCountDownDisplay.text = activityTimeString
    }

    fun updatePauseCountDownDisplay(timeInMsec:Int) {
        val timeToDescriptiveTime = millisecondsToDescriptiveTime(timeInMsec)
        val activityTimeString = "Pause Time : $timeToDescriptiveTime"
        pauseCountDownDisplay.text = activityTimeString
    }

    fun updatesSessionsLeft(num:Int) {
        if(!(numberOfReps <= 0 && num <= -1)) {
            numberOfReps += num
        }
    }
}