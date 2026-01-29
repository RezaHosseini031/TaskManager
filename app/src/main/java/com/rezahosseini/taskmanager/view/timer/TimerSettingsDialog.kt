package com.rezahosseini.taskmanager.view.timer

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.TimerPrefs

class TimerSettingsDialog(
    context: Context,
    private val onSave: (focus: Int, breakTime: Int) -> Unit
) : Dialog(context) {

    private lateinit var focusPicker: NumberPicker
    private lateinit var breakPicker: NumberPicker
    private lateinit var cancelBtn: Button
    private lateinit var saveBtn: Button
    private val prefs = TimerPrefs(context)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_timer_settings)

        focusPicker = findViewById(R.id.focusPicker)
        breakPicker = findViewById(R.id.breakPicker)
        cancelBtn = findViewById(R.id.cancelBtn)
        saveBtn = findViewById(R.id.saveBtn)

        focusPicker.minValue = 1
        focusPicker.maxValue = 120
        focusPicker.value = prefs.getFocusMinutes()

        breakPicker.minValue = 1
        breakPicker.maxValue = 60
        breakPicker.value = prefs.getBreakMinutes()

        cancelBtn.setOnClickListener { dismiss() }

        saveBtn.setOnClickListener {
            prefs.setFocusMinutes(focusPicker.value)
            prefs.setBreakMinutes(breakPicker.value)
            onSave(focusPicker.value, breakPicker.value)
            dismiss()
        }
    }
}
