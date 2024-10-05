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
import com.github.repos.presentation.howtoscreen.HowToScreen
import com.github.repos.presentation.auth.forgotpassword.ForgotPasswordScreen
import com.github.repos.presentation.auth.login.LoginScreen
import com.github.repos.presentation.auth.register.RegisterScreen
import com.github.repos.presentation.auth.welcome.WelcomeScreen
import com.github.repos.presentation.components.AppTopBar
import com.github.repos.presentation.components.BottomNavigationBar
import com.github.repos.presentation.components.NavigationDrawer
import com.github.repos.presentation.navigation.RepoDetails.arguments
import com.github.repos.presentation.navigation.RepoDetails.avatarUrlArg
import com.github.repos.presentation.navigation.RepoDetails.repoNameArg
import com.github.repos.presentation.navigation.RepoDetails.userNameArg
import com.github.repos.presentation.repodetails.RepositoryDetailsScreen
import com.github.repos.presentation.summary.SummaryScreen

@Composable
fun RootNav(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AuthNav.route
    ) {
        authNav(navController)
        dashboardNav(navController)
    }
}

fun NavGraphBuilder.authNav(
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
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
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

fun NavGraphBuilder.dashboardNav(
    navController: NavHostController
) {
    navigation(
        route = HomeNav.route,
        startDestination = Summary.route
    ) {

        composable(route = Summary.route) {
            DashboardScreens(navController) {
                SummaryScreen(navController = navController)
            }
        }

        composable(route = AllRepos.route) {
            DashboardScreens(navController) {
                AllRepositoriesScreen(navController = navController)
            }
        }
        otherNav(navController = navController)
    }
}

fun NavGraphBuilder.otherNav(
    navController: NavHostController
) {
    navigation(
        startDestination = Empty.route,
        route = OtherNav.route
    ) {
        composable(route = Empty.route) {
        }
        composable(
            route = RepoDetails.routeWithArgs,
            arguments = RepoDetails.arguments,
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
            RepositoryDetailsScreen(navController, userName ?: "", repoName ?: "", avatarUrl ?: "")
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


@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavHostController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun NavHostController.navigateWebView(url: String, title: String) {
    val encodedUrl = Uri.encode(url)
    this.navigateWithStackControl("${HowTo.route}/${title ?: ""}/${encodedUrl ?: ""}")
}