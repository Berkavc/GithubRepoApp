package com.github.repos

import AllRepositoriesScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.repos.SingleRepo.arguments
import com.github.repos.SingleRepo.avatarUrlArg
import com.github.repos.SingleRepo.repoNameArg
import com.github.repos.SingleRepo.userNameArg
import com.github.repos.presentation.RepositoryDetailsScreen
import com.github.repos.presentation.SummaryScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String = Summary.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination =  startDestination,
        modifier = modifier
    ) {
        composable(route = Summary.route) {
            SummaryScreen(navController)
        }
        composable(route = AllRepos.route) {
            AllRepositoriesScreen(navController)
        }
        composable(
            route = SingleRepo.routeWithArgs,
            arguments = arguments,
//            deepLinks = SingleRepo.deepLinks
        ) { navBackStackEntry ->
            val repoName =
                navBackStackEntry.arguments?.getString(repoNameArg)
            val userName =
                navBackStackEntry.arguments?.getString(userNameArg)
            val avatarUrl =
                navBackStackEntry.arguments?.getString(avatarUrlArg)
            RepositoryDetailsScreen(navController, userName ?: "", repoName ?: "", avatarUrl ?: "")
        }
    }
}

fun NavHostController.navigateWithStackControl(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateWithStackControl.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
fun NavHostController.navigateAndClearBackStack(route: String) =
    this.navigate(route) {
        popUpTo(0) {
            inclusive = true
        }
        launchSingleTop = true
    }

fun NavHostController.navigateDetails(userName: String, repoName: String, avatarUrl: String) {
    this.navigateWithStackControl("${SingleRepo.route}/${userName}/${repoName}/${avatarUrl}")
}
