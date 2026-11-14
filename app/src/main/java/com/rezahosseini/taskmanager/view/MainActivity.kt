package com.rezahosseini.taskmanager.view

import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import dagger.hilt.android.AndroidEntryPoint
import com.rezahosseini.taskmanager.ui.theme.TaskManagerTheme
import com.rezahosseini.taskmanager.view.navigation.AppNavigation


@AndroidEntryPoint
class MainActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskManagerTheme {
                AppNavigation()
            }
        }
    }
}