package com.github.repos

import AllRepositoriesScreen
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.github.repos.SingleRepo.arguments
import com.github.repos.SingleRepo.avatarUrlArg
import com.github.repos.SingleRepo.repoNameArg
import com.github.repos.SingleRepo.userNameArg
import com.github.repos.presentation.RepositoryDetailsScreen
import com.github.repos.presentation.SummaryScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination =  Summary.route,
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
            arguments = SingleRepo.arguments,
//            deepLinks = SingleRepo.deepLinks
        ) { navBackStackEntry ->
            arguments.forEach {
                navBackStackEntry.arguments?.getString(it.name)
            }
            val repoName =
                navBackStackEntry.arguments?.getString(repoNameArg)
            val userName =
                navBackStackEntry.arguments?.getString(userNameArg)
            val avatarUrl =
                navBackStackEntry.arguments?.getString(avatarUrlArg)
            if (repoName != null && userName != null && avatarUrl != null) {
                RepositoryDetailsScreen(navController, userName, repoName, avatarUrl)
            } else {
                Text("Error: Missing required arguments")
            }
        }
    }
}

fun NavHostController.navigateWithStackControl(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateWithStackControl.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
fun NavHostController.navigateAndClearBackStack(route: String) =
    this.navigate(route) {
        // Pop up to the root of the backstack and clear everything
        popUpTo(0) {
            inclusive = true // Clears the backstack entirely
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
    }

fun NavHostController.navigateDetails(userName: String, repoName: String, avatarUrl: String) {
    val encodedAvatarUrl = Uri.encode(avatarUrl)
    this.navigateWithStackControl("${SingleRepo.route}/${userName}/${repoName}/${encodedAvatarUrl}")
}
