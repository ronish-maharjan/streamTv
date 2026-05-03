package com.streamtv.app.ui.home

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter

class SettingsItemPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val ctx = parent.context
        val tv = TextView(ctx).apply {
            layoutParams = ViewGroup.LayoutParams(
                dpToPx(ctx, 200),
                dpToPx(ctx, 52)
            )
            textSize = 13f
            setTextColor(0xFFCCCCDD.toInt())
            gravity = Gravity.CENTER
            isFocusable = true
            isFocusableInTouchMode = true
            background = buildBackground(ctx)
            letterSpacing = 0.05f
        }
        return ViewHolder(tv)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).text = (item as SettingsItem).label
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}

    private fun buildBackground(ctx: Context): StateListDrawable {
        val radius = dpToPx(ctx, 8).toFloat()

        val focused = GradientDrawable().apply {
            setColor(0xFF1E1E2E.toInt())
            cornerRadius = radius
            setStroke(dpToPx(ctx, 2), 0xFFE50914.toInt())
        }
        val normal = GradientDrawable().apply {
            setColor(0xFF12121A.toInt())
            cornerRadius = radius
            setStroke(dpToPx(ctx, 1), 0xFF222233.toInt())
        }
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_focused), focused)
            addState(intArrayOf(), normal)
        }
    }

    private fun dpToPx(ctx: Context, dp: Int): Int =
        (dp * ctx.resources.displayMetrics.density).toInt()
}
