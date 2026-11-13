package com.rezahosseini.taskmanager.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rezahosseini.taskmanager.view.tasks.TasksScreen
import com.rezahosseini.taskmanager.view.timer.TimerScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Tasks.route
    ) {

        composable(Screen.Tasks.route) {
            TasksScreen(
                onNavigateToTimer = {
                    navController.navigate(Screen.Timer.route)
                }
            )
        }

        composable(Screen.Timer.route) {
            TimerScreen()
        }

    }
}