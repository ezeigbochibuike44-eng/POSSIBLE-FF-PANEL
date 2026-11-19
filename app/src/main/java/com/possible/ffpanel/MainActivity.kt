package com.possible.ffpanel

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {
    private val bg = Color.rgb(8, 12, 25)
    private val cyan = Color.rgb(120, 225, 255)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.decorView.systemUiVisibility = 0
        render()
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun render() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(22), dp(28), dp(22), dp(24))
            background = GlassUi.gradient(intArrayOf(
                Color.rgb(8, 14, 32), Color.rgb(22, 31, 58), Color.rgb(8, 12, 25)
            ), 0)
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        content.addView(label("POSSIBLE", 14, cyan, true))
        content.addView(label("FF PANEL", 32, Color.WHITE, true))
        content.addView(label("LIQUID GLASS • GAMING COMPANION", 11, 0xFFB9C8DF.toInt(), false))
        content.addView(space(22))
        content.addView(GlassUi.card(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(18), dp(18), dp(18), dp(18))
            addView(label("Your floating toolkit", 20, Color.WHITE, true))
            addView(label("A clean companion panel for practice tools and personal settings. It does not modify Free Fire.", 13, 0xFFD2DDED.toInt(), false))
            addView(space(12))
            addView(actionButton("LAUNCH FLOATING PANEL") { launchPanel() })
        })
        content.addView(space(16))
        content.addView(section("TRAINING TOOLS"))
        content.addView(toolCard("🎯  Headshot practice", "Start a 60-second reaction and target practice timer.") {
            startActivity(Intent(this, TrainingActivity::class.java).putExtra("mode", "Headshot practice"))
        })
        content.addView(toolCard("⚙️  Sensitivity notebook", "Save your own tested sensitivity values. No game files are changed.") {
            startActivity(Intent(this, TrainingActivity::class.java).putExtra("mode", "Sensitivity notebook"))
        })
        content.addView(toolCard("🖐  HUD layout notes", "Keep notes about your preferred button layout.") {
            startActivity(Intent(this, TrainingActivity::class.java).putExtra("mode", "HUD layout notes"))
        })
        content.addView(toolCard("🚀  Performance checklist", "Quick Android settings to check before playing.") {
            startActivity(Intent(this, TrainingActivity::class.java).putExtra("mode", "Performance checklist"))
        })
        content.addView(space(14))
        content.addView(label("Designed by POSSIBLE TECHNOLOGIES", 11, 0xFF91A5C5.toInt(), false))
        setContentView(root)
    }

    private fun launchPanel() {
        if (!Settings.canDrawOverlays(this)) {
            val i = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName"))
            startActivity(i)
            Toast.makeText(this, "Allow 'Display over other apps', then tap Launch again.", Toast.LENGTH_LONG).show()
            return
        }
        val intent = Intent(this, FloatingPanelService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
        Toast.makeText(this, "Floating panel started. You can return to your game.", Toast.LENGTH_SHORT).show()
    }

    private fun section(s: String) = label(s, 12, cyan, true).apply {
        setPadding(0, dp(8), 0, dp(8))
    }

    private fun toolCard(title: String, desc: String, click: () -> Unit): View {
        val card = GlassUi.card(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(14), dp(16), dp(14))
            isClickable = true
            isFocusable = true
            setOnClickListener { click() }
        }
        card.addView(label(title, 16, Color.WHITE, true))
        card.addView(label(desc, 12, 0xFFC2CEE0.toInt(), false))
        val lp = LinearLayout.LayoutParams(-1, -2); lp.bottomMargin = dp(10)
        card.layoutParams = lp
        return card
    }

    private fun actionButton(text: String, click: () -> Unit) = Button(this).apply {
        this.text = text
        isAllCaps = false
        setTextColor(Color.rgb(5, 18, 34))
        textSize = 13f
        typeface = Typeface.DEFAULT_BOLD
        background = GlassUi.gradient(intArrayOf(0xFF9DEBFF.toInt(), 0xFFB9C8FF.toInt()), 18)
        setOnClickListener { click() }
    }

    private fun label(s: String, size: Int, color: Int, bold: Boolean) = TextView(this).apply {
        text = s; textSize = size.toFloat(); setTextColor(color)
        if (bold) typeface = Typeface.DEFAULT_BOLD
        setPadding(0, dp(3), 0, dp(3))
    }
    private fun space(h: Int) = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, dp(h)) }
}
