package com.github.repos.presentation.navigation

import AllRepositoriesScreen
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.repos.HowToScreen
import com.github.repos.presentation.extensions.ViewExt
import com.github.repos.presentation.navigation.RepoDetails.arguments
import com.github.repos.presentation.navigation.RepoDetails.avatarUrlArg
import com.github.repos.presentation.navigation.RepoDetails.repoNameArg
import com.github.repos.presentation.navigation.RepoDetails.userNameArg
import com.github.repos.presentation.repodetails.RepositoryDetailsScreen
import com.github.repos.presentation.summary.SummaryScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Summary.route,
        modifier = modifier
    ) {
        composable(route = Summary.route) {
            SummaryScreen(navController)
         /*   {
                ViewExt.openWebView(navController, "https://developer.android.com/?hl=tr", "Android Developer")
            }*/
        }
        composable(route = AllRepos.route) {
            AllRepositoriesScreen(navController)
        }
        composable(
            route = RepoDetails.routeWithArgs,
            arguments = RepoDetails.arguments
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
            RepositoryDetailsScreen(navController, userName ?: "", repoName ?: "", avatarUrl ?: "")
        }
        composable(
            route = HowTo.routeWithArgs,
            arguments = HowTo.arguments
        ) { navBackStackEntry ->
            val title = navBackStackEntry.arguments?.getString("title") ?: ""
            val url = navBackStackEntry.arguments?.getString("url") ?: ""
            HowToScreen(navController = navController, title = title, url = url)
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
    val encodedAvatarUrl = Uri.encode(avatarUrl)
    this.navigateWithStackControl("${RepoDetails.route}/${userName}/${repoName}/${encodedAvatarUrl}")
}

fun NavHostController.navigateWebView(url: String, title: String) {
    val encodedUrl = Uri.encode(url)
    this.navigateWithStackControl("${HowTo.route}/${title ?: ""}/${encodedUrl ?: ""}")
}