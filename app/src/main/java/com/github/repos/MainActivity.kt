package com.github.repos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.repos.domain.model.AllRepositories
import com.github.repos.presentation.SummaryScreen
import com.github.repos.presentation.components.AppTopBar
import com.github.repos.presentation.components.BottomNavigationBar
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
    val currentScreen =
        allDestinations.find { it.route == currentDestination?.route } ?: Summary


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
            BottomNavigationBar(
                allScreens = bottomNavDestinations,
                onTabSelected ={ newScreen ->
                    navController.navigateSingleTopTo(newScreen.route) /*todo*/
                },
                currentScreen = currentScreen,
                navController)
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
//        NavHost(
//            navController = navController,
//            startDestination = Summary.route,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable(route = Summary.route) {
//                SummaryScreen(navController)
//            }
//            composable(route = AllRepositories.route) {
//                AllRepositoriesScreen(navController)
//            }
//        }
    }
}