package com.github.repos.presentation.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.repos.presentation.navigation.ForgotPassword
import com.github.repos.presentation.navigation.HomeNav
import com.github.repos.presentation.navigation.Login
import com.github.repos.presentation.navigation.Register

@Composable
fun LoginScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: LoginViewModel = hiltViewModel()
) {
    val username = remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Login.title),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Normal,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.size(100.dp))

            Button(onClick = {
                //Navigate to Home Screen with Separate navhost
                navController.navigate(HomeNav.route)
            }) {
                Text(text = "Go To Home")
            }
            Button(onClick = {
                //Navigate to Home Screen with Separate navhost
                navController.navigate(ForgotPassword.route)
            }) {
                Text(text = "Go To Forgot Password")
            }
            Button(onClick = {
                //Navigate to Home Screen with Separate navhost
                navController.navigate(Register.route)
            }) {
                Text(text = "Go To Register")
            }
        }

    }
}