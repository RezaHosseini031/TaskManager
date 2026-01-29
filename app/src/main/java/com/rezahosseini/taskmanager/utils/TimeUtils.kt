package com.rezahosseini.taskmanager.utils

object TimeUtils {
    fun formatTime(seconds: Int): String {
        val m = seconds / 60
        val s = seconds % 60
        return String.format("%02d:%02d", m, s)
    }
}
