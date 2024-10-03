package com.github.repos

import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

sealed interface AppDestination {
    val route : String
    val title: Int
    val icon: Int
}

object Welcome : AppDestination {
    override val route: String = "welcome"
    override val title = R.string.welcome
    override val icon: Int = R.drawable.rounded_edge
}

object Login : AppDestination {
    override val route: String = "login"
    override val title = R.string.login
    override val icon: Int = R.drawable.rounded_edge
}

object Register : AppDestination {
    override val route: String = "register"
    override val title = R.string.register
    override val icon: Int = R.drawable.rounded_edge
}

object ForgotPassword : AppDestination {
    override val route: String = "forgot_password"
    override val title = R.string.forgot_password
    override val icon: Int = R.drawable.rounded_edge
}

object AuthNav : AppDestination {
    override val route: String = "auth"
    override val title = R.string.auth_graph
    override val icon: Int = R.drawable.rounded_edge
}

object HomeNav : AppDestination {
    override val route: String = "home"
    override val title = R.string.home_graph
    override val icon: Int = R.drawable.rounded_edge
}

object OtherNav : AppDestination {
    override val route: String = "other"
    override val title = R.string.other_graph
    override val icon: Int = R.drawable.rounded_edge
}

object Empty : AppDestination {
    override val route: String = "empty"
    override val title = R.string.other_graph
    override val icon: Int = R.drawable.rounded_edge
}
object Summary : AppDestination {
    override val route: String = "summary"
    override val title = R.string.summary
    override val icon: Int = R.drawable.rounded_edge
}
object AllRepos : AppDestination {
    override val route: String = "all_repositories"
    override val title = R.string.all_repositories
    override val icon: Int =  R.drawable.ic_launcher_background
}


object SingleRepo : AppDestination {
    override val route = "single_repository"
    override val icon = R.drawable.ic_launcher_background
    override val title = R.string.repository_detail
    const val userNameArg = "user_name"
    const val repoNameArg = "repo_name"
    const val avatarUrlArg = "avatar_url"
    // Define the dynamic route with placeholders
    val routeWithArgs = "$route/{$userNameArg}/{$repoNameArg}/{$avatarUrlArg}"

    // Define arguments to be passed via the route
    val arguments = listOf(
        navArgument(userNameArg) { type = NavType.StringType },
        navArgument(repoNameArg) { type = NavType.StringType },
        navArgument(avatarUrlArg) { type = NavType.StringType }
    )
//    val deepLinks = listOf(
//        navDeepLink { uriPattern = "android-app://androidx.navigation/$route/{$userNameArg}/{$repoNameArg}/{$avatarUrlArg}" }
//    )
}

val allDestinations = listOf(Summary, AllRepos, SingleRepo)

val bottomNavDestinations = listOf(Summary, AllRepos)

//todo drawer