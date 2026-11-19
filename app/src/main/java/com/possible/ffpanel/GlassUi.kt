package com.possible.ffpanel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.LinearLayout

object GlassUi {
    fun gradient(colors: IntArray, radiusDp: Int): GradientDrawable =
        GradientDrawable(GradientDrawable.Orientation.TL_BR, colors).apply {
            cornerRadius = radiusDp * 3f
            setStroke(1, 0x66D8F3FF)
        }

    fun card(context: Context): LinearLayout {
        val d = context.resources.displayMetrics.density
        return LinearLayout(context).apply {
            background = gradient(intArrayOf(0xCC263B59.toInt(), 0xCC17243D.toInt()), 22)
            elevation = 5 * d
        }
    }
}
