
package com.rezahosseini.taskmanager.view.components

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.rezahosseini.taskmanager.R

private const val GITHUB_URL =
    "https://github.com/RezaHosseini031/TaskManager"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String
) {
    var expanded by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(text = title)
        },
        actions = {
            Box {
                IconButton(
                    onClick = { expanded = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More"
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.about_us))
                        },
                        onClick = {
                            expanded = false
                            showAboutDialog = true
                        }
                    )

                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.share))
                        },
                        onClick = {
                            expanded = false

                            val intent = Intent(
                                Intent.ACTION_SEND
                            ).apply {
                                type = "text/plain"
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "💻 سورس برنامه روی گیت‌هاب 👇\n$GITHUB_URL"
                                )
                            }

                            context.startActivity(
                                Intent.createChooser(
                                    intent,
                                    "اشتراک‌گذاری"
                                )
                            )

                            Toast.makeText(
                                context,
                                "share",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }
        }
    )

    if (showAboutDialog) {
        AboutUsDialog(
            onDismiss = {
                showAboutDialog = false
            },
            onMoreClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(GITHUB_URL)
                )
                context.startActivity(intent)
            }
        )
    }
}
