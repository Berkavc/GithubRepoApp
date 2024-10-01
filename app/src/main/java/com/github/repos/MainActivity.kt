package com.github.repos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.repos.presentation.components.AppTopBar
import com.github.repos.presentation.components.BottomNavigationBar
import com.github.repos.presentation.components.NavigationDrawer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StartScreen()
        }
    }
}

@Composable
fun StartScreen(
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    // Get the name of the current screen
    val currentScreen = remember(currentDestination) {
        allDestinations.find { it.route == currentDestination?.route } ?: Summary
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AppTopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        },
        bottomBar = {
            if (currentScreen in bottomNavDestinations) {
                BottomNavigationBar(
                    allScreens = bottomNavDestinations,
                    onTabSelected ={ newScreen ->
                    navController.navigateAndClearBackStack(newScreen.route)
                    },
                    currentScreen = currentScreen,
                    navController)
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            startDestination = currentScreen.route,
            modifier = Modifier.padding(innerPadding)
        )
    }
}