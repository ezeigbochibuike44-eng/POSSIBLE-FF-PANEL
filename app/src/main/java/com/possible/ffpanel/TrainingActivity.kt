package com.possible.ffpanel

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.*

class TrainingActivity : Activity() {
    private var timer: CountDownTimer? = null
    private lateinit var status: TextView
    private lateinit var input: EditText
    private var remaining = 60

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mode = intent.getStringExtra("mode") ?: "Training"
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 36, 28, 24)
            background = GlassUi.gradient(intArrayOf(0xFF091126.toInt(), 0xFF1A2B4A.toInt()), 0)
        }
        root.addView(TextView(this).apply {
            text = "POSSIBLE FF PANEL\n$mode"
            textSize = 23f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
        })
        root.addView(TextView(this).apply {
            text = when (mode) {
                "Headshot practice" -> "Practice reaction speed away from the game. Tap Start and use the 60-second focus session."
                "Sensitivity notebook" -> "Record values you have tested yourself. These are notes only; the app will not change game settings."
                "HUD layout notes" -> "Write down your preferred fire button size, placement and control layout."
                else -> "Close unused apps, keep enough free storage, avoid overheating, and use stable network connectivity."
            }
            textSize = 15f; setTextColor(0xFFD7E5F7.toInt()); setPadding(0, 24, 0, 24)
        })
        status = TextView(this).apply {
            text = "Ready"; textSize = 30f; setTextColor(0xFF9DEBFF.toInt()); gravity = Gravity.CENTER
        }
        root.addView(status)
        input = EditText(this).apply {
            hint = "Write your notes / values here"
            setTextColor(Color.WHITE); setHintTextColor(0xFFB7C5D8.toInt())
            setSingleLine(false); minLines = 3
            setBackgroundColor(0x553A5275)
        }
        if (mode != "Headshot practice" && mode != "Performance checklist") root.addView(input)
        val start = Button(this).apply {
            text = if (mode == "Headshot practice") "Start 60-second session" else "Save notes on this screen"
            setOnClickListener {
                if (mode == "Headshot practice") startTimer()
                else Toast.makeText(this@TrainingActivity, "Keep this screen open to refer to your notes.", Toast.LENGTH_SHORT).show()
            }
        }
        root.addView(start)
        root.addView(Button(this).apply { text = "Back"; setOnClickListener { finish() } })
        setContentView(root)
    }

    private fun startTimer() {
        timer?.cancel()
        timer = object : CountDownTimer(60_000, 1_000) {
            override fun onTick(ms: Long) {
                remaining = (ms / 1000).toInt()
                status.text = "${remaining}s"
            }
            override fun onFinish() { status.text = "Session complete"; }
        }.start()
    }
    override fun onDestroy() { timer?.cancel(); super.onDestroy() }
}
