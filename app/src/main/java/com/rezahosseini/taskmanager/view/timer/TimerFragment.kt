package com.rezahosseini.taskmanager.view.timer

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.TimerPrefs
import com.rezahosseini.taskmanager.vm.TimerViewModel
import com.rezahosseini.taskmanager.utils.TimeUtils.formatTime

class TimerFragment : Fragment(R.layout.fragment_timer) {

    private val viewModel: TimerViewModel by viewModels()

    private lateinit var tvTime: TextView
    private lateinit var tvMode: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnRestart: Button
    private lateinit var circularProgress: CircularProgressIndicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prefs = TimerPrefs(requireContext())
        tvTime = view.findViewById(R.id.tvTime)
        tvMode = view.findViewById(R.id.tvMode)
        btnStart = view.findViewById(R.id.btnStart)
        btnStop = view.findViewById(R.id.btnStop)
        btnRestart = view.findViewById(R.id.btnRestart)
        circularProgress = view.findViewById(R.id.circularProgress)

        viewModel.startTimer(requireContext(),prefs.getFocusMinutes()*60, prefs.getBreakMinutes()*60)
        viewModel.stopTimer()
        viewModel.mode.observe(viewLifecycleOwner) { mode ->
            tvMode.text = when (mode) {
                TimerViewModel.Mode.IDLE -> context?.getString(R.string.idle_time)
                TimerViewModel.Mode.FOCUS -> context?.getString(R.string.focus_time)
                TimerViewModel.Mode.BREAK -> context?.getString(R.string.break_time)
            }
        }

        viewModel.remainingSeconds.observe(viewLifecycleOwner) { sec ->
            tvTime.text = formatTime(sec)
        }

        viewModel.progress.observe(viewLifecycleOwner) { p ->
            circularProgress.progress = p
        }

        viewModel.isRunning.observe(viewLifecycleOwner) { running ->
            btnStart.isEnabled = !running
            btnStop.isEnabled = running
        }
        tvTime.setOnClickListener {
            TimerSettingsDialog(requireContext()) { focus, breakTime ->
                viewModel.startTimer(requireContext(),focus*60, breakTime*60)
            }.show()
        }
        btnStart.setOnClickListener {
            viewModel.resume(requireContext())
        }

        btnStop.setOnClickListener { viewModel.stopTimer() }
        btnRestart.setOnClickListener { viewModel.restart(requireContext()) }
    }
}
