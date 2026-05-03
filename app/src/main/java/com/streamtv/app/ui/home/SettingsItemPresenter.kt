package com.streamtv.app.ui.home

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.Presenter

class SettingsItemPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val tv = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(200, 52.dpToPx(parent.context))
            textSize = 13f
            setTextColor(0xFFCCCCDD.toInt())
            gravity = Gravity.CENTER
            isFocusable = true
            isFocusableInTouchMode = true
            background = buildBackground()
            letterSpacing = 0.05f
        }
        return ViewHolder(tv)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        (viewHolder.view as TextView).text = (item as SettingsItem).label
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}

    private fun buildBackground(): StateListDrawable {
        val focused = GradientDrawable().apply {
            setColor(0xFF1E1E2E.toInt())
            cornerRadius = 8f.dpToPx()
            setStroke(2.dpToPx().toInt(), 0xFFE50914.toInt())
        }
        val normal = GradientDrawable().apply {
            setColor(0xFF12121A.toInt())
            cornerRadius = 8f.dpToPx()
            setStroke(1.dpToPx().toInt(), 0xFF222233.toInt())
        }
        return StateListDrawable().apply {
            addState(intArrayOf(android.R.attr.state_focused), focused)
            addState(intArrayOf(), normal)
        }
    }

    private fun Int.dpToPx(context: android.content.Context): Int =
        (this * context.resources.displayMetrics.density).toInt()

    private fun Float.dpToPx(): Float =
        this * android.content.res.Resources.getSystem().displayMetrics.density
}
