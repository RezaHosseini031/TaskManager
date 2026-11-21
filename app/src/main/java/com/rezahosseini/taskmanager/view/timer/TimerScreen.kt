
package com.rezahosseini.taskmanager.view.timer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.timer.TimerPrefs
import com.rezahosseini.taskmanager.vm.TimerViewModel

@Composable
fun TimerScreen(
    viewModel: TimerViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefs = remember { TimerPrefs(context) }

    val mode by viewModel.mode.observeAsState(
        TimerViewModel.Mode.IDLE
    )

    val remainingSeconds by viewModel.remainingSeconds.observeAsState(0)
    val progress by viewModel.progress.observeAsState(0)
    val isRunning by viewModel.isRunning.observeAsState(false)

    var showSettingsDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        viewModel.initializeTimer(
            focusSeconds = prefs.getFocusMinutes() * 60,
            breakSeconds = prefs.getBreakMinutes() * 60
        )
    }

    val modeText = when (mode) {
        TimerViewModel.Mode.IDLE ->
            stringResource(R.string.idle_time)

        TimerViewModel.Mode.FOCUS ->
            stringResource(R.string.focus_time)

        TimerViewModel.Mode.BREAK ->
            stringResource(R.string.break_time)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Timer progress
        Box(
            modifier = Modifier.size(300.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = {
                    (progress / 100f).coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                strokeWidth = 8.dp
            )

            Text(
                text = String.format(
                    "%02d:%02d",
                    remainingSeconds / 60,
                    remainingSeconds % 60
                ),
                fontSize = 52.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.clickable {
                    showSettingsDialog = true
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = modeText,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Timer controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    viewModel.startAllTimers(context)
                },
                enabled = !isRunning && remainingSeconds > 0,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.start))
            }

            Button(
                onClick = {
                    // Pause the UI timer and stop its notification/alarm companions.
                    viewModel.stopTimer()
                    viewModel.pauseBackgroundTimer(context)
                    viewModel.cancelAlarmTimer(context)
                    viewModel.pauseBackgroundTimer(context)
                    viewModel.cancelAlarmTimer(context)
                },
                enabled = isRunning,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.stop))
            }

            Button(
                onClick = {
                    viewModel.restartAllTimers(context)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.restart))
            }
        }
    }

    // Timer settings dialog
    if (showSettingsDialog) {
        TimerSettingsDialog(
            onDismiss = {
                showSettingsDialog = false
            },
            onSave = { focus, breakTime ->

                prefs.setFocusMinutes(focus)
                prefs.setBreakMinutes(breakTime)

                // Stop old timers before applying new durations
                viewModel.stopTimer()
                viewModel.pauseBackgroundTimer(context)
                viewModel.cancelAlarmTimer(context)

                viewModel.updateDurationsAndReset(
                    focusSeconds = focus * 60,
                    breakSeconds = breakTime * 60
                )

                showSettingsDialog = false
            }
        )
    }
}
