package com.github.repos.presentation.summary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController

@Composable
fun SummaryScreen(
    navController: NavController = rememberNavController(),
    viewModel: SummaryViewModel = hiltViewModel()
) {
    Column {
        Text(
            text = "Summary_Screen",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray,
            modifier = Modifier.wrapContentSize(Alignment.Center)
        )

     /*   Button(onClick = {
            navigateNext()
        }) {
            Text(text = "Go to HowToScreen")
        }*/
    }
}