package com.rezahosseini.taskmanager.view.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ListAlt
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val screen: Screen,
    val title: String,
    val icon: ImageVector
)

val bottomNavItems = listOf(

    BottomNavItem(
        screen = Screen.Tasks,
        title = "Tasks",
        icon = Icons.Outlined.ListAlt
    ),

    BottomNavItem(
        screen = Screen.Timer,
        title = "Timer",
        icon = Icons.Outlined.Timer
    )
)