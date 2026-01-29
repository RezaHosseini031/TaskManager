package com.rezahosseini.taskmanager.model.local



import android.content.Context

class TimerPrefs(context: Context) {

    private val prefs = context.getSharedPreferences("timer_prefs", Context.MODE_PRIVATE)

    fun getFocusMinutes() = prefs.getInt("focus_minutes", 25)
    fun getBreakMinutes() = prefs.getInt("break_minutes", 5)

    fun setFocusMinutes(value: Int) {
        prefs.edit().putInt("focus_minutes", value).apply()
    }

    fun setBreakMinutes(value: Int) {
        prefs.edit().putInt("break_minutes", value).apply()
    }
}