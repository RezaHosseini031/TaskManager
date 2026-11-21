
package com.rezahosseini.taskmanager.view.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.timer.TimerPrefs
import kotlin.math.roundToInt

@Composable
fun TimerSettingsDialog(
    onDismiss: () -> Unit,
    onSave: (focus: Int, breakTime: Int) -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { TimerPrefs(context) }

    var focusMinutes by rememberSaveable {
        mutableIntStateOf(prefs.getFocusMinutes())
    }

    var breakMinutes by rememberSaveable {
        mutableIntStateOf(prefs.getBreakMinutes())
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.timer),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                // Focus time
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.Focus_min)
                        )

                        Text(
                            text = "$focusMinutes",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Slider(
                        value = focusMinutes.toFloat(),
                        onValueChange = {
                            focusMinutes = it.roundToInt()
                        },
                        valueRange = 1f..120f,
                        steps = 118
                    )
                }

                // Break time
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.Break_min)
                        )

                        Text(
                            text = "$breakMinutes",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Slider(
                        value = breakMinutes.toFloat(),
                        onValueChange = {
                            breakMinutes = it.roundToInt()
                        },
                        valueRange = 1f..60f,
                        steps = 58
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    prefs.setFocusMinutes(focusMinutes)
                    prefs.setBreakMinutes(breakMinutes)

                    onSave(focusMinutes, breakMinutes)
                    onDismiss()
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
