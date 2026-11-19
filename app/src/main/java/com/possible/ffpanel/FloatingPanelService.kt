package com.possible.ffpanel

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Typeface
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.*
import android.graphics.drawable.GradientDrawable

class FloatingPanelService : Service() {
    private lateinit var wm: WindowManager
    private var panel: View? = null
    private val channelId = "possible_ff_panel"

    override fun onCreate() {
        super.onCreate()
        createNotification()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        showPanel()
    }

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "POSSIBLE FF PANEL", NotificationManager.IMPORTANCE_LOW)
            (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        }
        val notification = if (Build.VERSION.SDK_INT >= 26)
            Notification.Builder(this, channelId)
        else Notification.Builder(this)
        notification.setContentTitle("POSSIBLE FF PANEL")
            .setContentText("Floating companion panel is active")
            .setSmallIcon(android.R.drawable.ic_menu_view)
            .setOngoing(true)
        startForeground(7, notification.build())
    }

    private fun showPanel() {
        if (panel != null) return
        val density = resources.displayMetrics.density
        fun dp(v: Int) = (v * density).toInt()
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                intArrayOf(0xF02A4264.toInt(), 0xF014203B.toInt())
            ).apply { cornerRadius = dp(22).toFloat(); setStroke(dp(1), 0xCCB8EDFF.toInt()) }
            setPadding(dp(12), dp(10), dp(12), dp(12))
            elevation = dp(12).toFloat()
        }
        val header = LinearLayout(this).apply { gravity = Gravity.CENTER_VERTICAL }
        val title = TextView(this).apply {
            text = "✦ POSSIBLE FF"; textSize = 14f; setTextColor(Color.WHITE)
            typeface = Typeface.DEFAULT_BOLD
        }
        header.addView(title, LinearLayout.LayoutParams(0, dp(38), 1f))
        val minimize = TextView(this).apply {
            text = "—"; textSize = 22f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
        }
        val close = TextView(this).apply {
            text = "×"; textSize = 24f; setTextColor(Color.WHITE); gravity = Gravity.CENTER
        }
        header.addView(minimize, LinearLayout.LayoutParams(dp(36), dp(38)))
        header.addView(close, LinearLayout.LayoutParams(dp(32), dp(38)))
        container.addView(header)

        val body = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        fun item(label: String, detail: String) {
            val b = Button(this).apply {
                text = label
                isAllCaps = false
                textSize = 12f
                setOnClickListener {
                    Toast.makeText(this@FloatingPanelService, detail, Toast.LENGTH_SHORT).show()
                }
            }
            body.addView(b, LinearLayout.LayoutParams(-1, dp(42)))
        }
        item("🎯  Practice timer", "Open POSSIBLE FF PANEL to start a 60-second practice session.")
        item("⚙️  Sensitivity notes", "Use the app dashboard to record your tested sensitivity values.")
        item("🚀  Performance tips", "Close background apps and keep your phone cool.")
        container.addView(body)
        val footer = TextView(this).apply {
            text = "Drag this header • Companion only"
            textSize = 10f; setTextColor(0xFFD0E7F5.toInt()); gravity = Gravity.CENTER
        }
        container.addView(footer)

        val type = if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                   else WindowManager.LayoutParams.TYPE_PHONE
        val params = WindowManager.LayoutParams(
            dp(250), WindowManager.LayoutParams.WRAP_CONTENT, type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply { gravity = Gravity.TOP or Gravity.END; x = dp(12); y = dp(120) }

        var startX = 0; var startY = 0; var touchX = 0f; var touchY = 0f
        header.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startX = params.x; startY = params.y
                    touchX = event.rawX; touchY = event.rawY; true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = startX - (event.rawX - touchX).toInt()
                    params.y = startY + (event.rawY - touchY).toInt()
                    wm.updateViewLayout(container, params); true
                }
                else -> false
            }
        }
        minimize.setOnClickListener {
            body.visibility = if (body.visibility == View.GONE) View.VISIBLE else View.GONE
            footer.visibility = body.visibility
        }
        close.setOnClickListener { stopSelf() }
        panel = container
        try { wm.addView(container, params) } catch (_: Exception) { panel = null; stopSelf() }
    }

    override fun onDestroy() {
        panel?.let { try { wm.removeView(it) } catch (_: Exception) {} }
        panel = null
        stopForeground(STOP_FOREGROUND_REMOVE)
        super.onDestroy()
    }
    override fun onBind(intent: Intent?): IBinder? = null
}
