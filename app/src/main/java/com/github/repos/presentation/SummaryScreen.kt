package com.github.repos.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.github.repos.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
@Composable
fun SummaryScreen(
    navController: NavController,
//    viewModel: MainViewModel = hiltViewModel()
) {
    val viewModel = hiltViewModel<MainViewModel>()
    /*todo*/

}