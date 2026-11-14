
package com.rezahosseini.taskmanager.view.tasks

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rezahosseini.taskmanager.R

private enum class DeleteAction {
    ALL,
    DONE
}

@Composable
fun TasksTopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSortChange: (String) -> Unit,
    onDeleteAll: () -> Unit,
    onDeleteDone: () -> Unit
) {
    var sortExpanded by remember { mutableStateOf(false) }
    var deleteExpanded by remember { mutableStateOf(false) }
    var deleteAction by remember {
        mutableStateOf<DeleteAction?>(null)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Sort
        Box {
            IconButton(
                onClick = { sortExpanded = true }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Sort"
                )
            }

            DropdownMenu(
                expanded = sortExpanded,
                onDismissRequest = {
                    sortExpanded = false
                }
            ) {
                val sortOptions = listOf(
                    "AZ" to "A - Z",
                    "ZA" to "Z - A",
                    "PRIO_HIGH" to "Priority High",
                    "PRIO_LOW" to "Priority Low",
                    "DATE_NEW" to "Date New",
                    "DATE_OLD" to "Date Old"
                )

                sortOptions.forEach { (value, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            onSortChange(value)
                            sortExpanded = false
                        }
                    )
                }
            }
        }

        // Search
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text(stringResource(R.string.search_title))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(50.dp)
        )

        // Delete menu - old delete_history icon
        Box {
            Image(
                painter = painterResource(
                    id = R.drawable.delete_history
                ),
                contentDescription = "Delete tasks",
                colorFilter = ColorFilter.tint(
                    MaterialTheme.colorScheme.error
                ),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .clickable {
                        deleteExpanded = true
                    }
                    .padding(7.dp)
            )

            DropdownMenu(
                expanded = deleteExpanded,
                onDismissRequest = {
                    deleteExpanded = false
                }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.delete_all))
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                R.drawable.baseline_delete_sweep_24
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    onClick = {
                        deleteExpanded = false
                        deleteAction = DeleteAction.ALL
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(stringResource(R.string.delete_selected))
                    },
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                R.drawable.baseline_auto_delete_24
                            ),
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                    },
                    onClick = {
                        deleteExpanded = false
                        deleteAction = DeleteAction.DONE
                    }
                )
            }
        }
    }

    // Delete confirmation dialog
    deleteAction?.let { action ->

        val isDeleteAll = action == DeleteAction.ALL

        AlertDialog(
            onDismissRequest = {
                deleteAction = null
            },
            title = {
                Text(
                    if (isDeleteAll) {
                        "Delete all tasks?"
                    } else {
                        "Delete completed tasks?"
                    }
                )
            },
            text = {
                Text(
                    if (isDeleteAll) {
                        "Are you sure you want to delete all tasks?"
                    } else {
                        "Are you sure you want to delete all completed tasks?"
                    }
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (isDeleteAll) {
                            onDeleteAll()
                        } else {
                            onDeleteDone()
                        }

                        deleteAction = null
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        deleteAction = null
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}
