package com.github.repos.presentation.summary

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.repos.presentation.mainactivity.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun SummaryScreen(
    navController: NavController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
//    val viewModel = hiltViewModel<MainViewModel>()
    /*todo*/

}