package com.github.repos.presentation.navigation

import AllRepositoriesScreen
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.github.repos.presentation.navigation.SingleRepo.arguments
import com.github.repos.presentation.navigation.SingleRepo.avatarUrlArg
import com.github.repos.presentation.navigation.SingleRepo.repoNameArg
import com.github.repos.presentation.navigation.SingleRepo.userNameArg
import com.github.repos.presentation.auth.ForgotPasswordScreen
import com.github.repos.presentation.auth.LoginScreen
import com.github.repos.presentation.auth.RegisterScreen
import com.github.repos.presentation.repodetails.RepositoryDetailsScreen
import com.github.repos.presentation.summary.SummaryScreen
import com.github.repos.presentation.auth.WelcomeScreen
import com.github.repos.presentation.components.AppTopBar
import com.github.repos.presentation.components.BottomNavigationBar
import com.github.repos.presentation.components.NavigationDrawer

@Composable
fun RootNav(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthNav.route
    ) {
        AuthNav(navController)

        DashboardNav(navController)

//        composable(route = HomeNav.route) {
//            DashboardScreens()
//        }
    }
}

fun NavGraphBuilder.AuthNav(
    navController: NavHostController
) {
    navigation(
        startDestination = Welcome.route,
        route = AuthNav.route
    ) {
        composable(route = Welcome.route) {
            WelcomeScreen(navController = navController)
        }

        composable(route = Login.route) {
            LoginScreen(navController = navController)
        }

        composable(route = ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(route = Register.route) {
            RegisterScreen(navController = navController)
        }
    }
}

@Composable
fun DashboardScreens(
    navController: NavHostController = rememberNavController(),
    content: @Composable () -> Unit,
) {
    // Get current back stack entry
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    // Get the name of the current screen
    val currentScreen = remember(currentDestination) {
        allDestinations.find { it.route == currentDestination?.route } ?: Summary
    }
    NavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Log Out",
                    modifier = Modifier.clickable {
                        navController.navigateAndClearBackStack(AuthNav.route)
                    })
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (currentScreen in bottomNavDestinations) {
                    BottomNavigationBar(
                        allScreens = bottomNavDestinations,
                        onTabSelected = { newScreen ->
                            navController.navigateAndClearBackStack(newScreen.route)
                        },
                        currentScreen = currentScreen,
                        navController
                    )
                }
            },
            topBar = {
//                AppTopBar(
//                    currentScreen = currentScreen,
//                    canNavigateBack = navController.previousBackStackEntry != null,
//                    navigateUp = { navController.navigateUp() }
//                )
            }
        ) {
            content()
        }
    }
}

fun NavGraphBuilder.DashboardNav(
    navController: NavHostController
) {
    navigation(
        route = HomeNav.route,
        startDestination = Summary.route
    ) {

        composable(route = Summary.route) {
            DashboardScreens (navController) {
                SummaryScreen(navController = navController)
            }
        }

        composable(route = AllRepos.route) {
            DashboardScreens (navController) {
                AllRepositoriesScreen(navController = navController)
            }
        }

        OtherNav(navController = navController)
    }
}

@Composable
fun DashboardNav(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        route = HomeNav.route,
        startDestination = Summary.route
    ) {
        composable(route = Summary.route) {
            SummaryScreen(navController = navController)
        }

        composable(route = AllRepos.route) {
            AllRepositoriesScreen(navController = navController)
        }

        OtherNav(navController = navController)
    }
}

fun NavGraphBuilder.OtherNav(
    navController: NavHostController
) {
    navigation(
        startDestination = Empty.route,
        route = OtherNav.route
    ) {
        composable(route = Empty.route) {
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
                    onTabSelected = { newScreen ->
                        navController.navigateAndClearBackStack(newScreen.route)
                    },
                    currentScreen = currentScreen,
                    navController
                )
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

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}