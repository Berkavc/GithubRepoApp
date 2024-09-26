package com.github.repos

import AllRepositoriesScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.github.repos.domain.model.AllRepositories
import com.github.repos.presentation.SummaryScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Summary.route,
        modifier = modifier
    ) {
        composable(route = Summary.route) {
            SummaryScreen(navController)
        }
        composable(route = AllRepos.route) {
            AllRepositoriesScreen(navController)
//            SummaryScreen(navController)

        }
//        composable(
//            route = SingleRepository.routeWithArgs,
//            arguments = SingleRepository.arguments,
//            deepLinks = SingleRepository.deepLinks
//        ) { navBackStackEntry ->
//            val accountType =
//                navBackStackEntry.arguments?.getString(SingleRepository.accountTypeArg)
//            RepositoryDetailsScreen() /*todo*/
//        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }