package com.rezahosseini.taskmanager.view.navigation

sealed class Screen(val route: String) {

    data object Tasks : Screen("tasks")

    data object Timer : Screen("timer")

}