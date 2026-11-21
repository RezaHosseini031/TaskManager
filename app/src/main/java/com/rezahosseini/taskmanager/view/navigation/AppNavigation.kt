package com.rezahosseini.taskmanager.view.navigation


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.rezahosseini.taskmanager.view.components.MainTopBar
import com.rezahosseini.taskmanager.view.tasks.TasksScreen
import com.rezahosseini.taskmanager.view.timer.TimerScreen


@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route
    val title = when (currentRoute) {
        "tasks" -> "Works"
        "timer" -> "Timer"
        else -> "Task Manager"
    }
    Scaffold(
        topBar = {
            MainTopBar(
                title =title
            )
        },
        bottomBar = {

            NavigationBar {

                bottomNavItems.forEach { item ->

                    NavigationBarItem(

                        selected = currentRoute == item.screen.route,

                        onClick = {

                            navController.navigate(item.screen.route) {

                                popUpTo(
                                    navController.graph.findStartDestination().id
                                ) {
                                    saveState = true
                                }

                                launchSingleTop = true

                                restoreState = true
                            }
                        },

                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },

                        label = {
                            Text(text = item.title)
                        }
                    )
                }
            }
        }

    ) { innerPadding ->
            NavHost(

                navController = navController,

                startDestination = Screen.Tasks.route,

                modifier = Modifier.padding(innerPadding)

            ) {

                composable(Screen.Tasks.route) {
                    TasksScreen()
                }

                composable(Screen.Timer.route) {
                    TimerScreen()
                }

            }
        }
    }