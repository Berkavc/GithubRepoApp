package com.github.repos.presentation.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.github.repos.R

sealed interface AppDestination {
    val route: String
    val title: Int
    val icon: Int
}

object Summary : AppDestination {
    override val route: String = "summary"
    override val title = R.string.summary
    override val icon: Int = R.drawable.rounded_edge
}

object AllRepos : AppDestination {
    override val route: String = "all_repositories"
    override val title = R.string.all_repositories
    override val icon: Int = R.drawable.ic_launcher_background
}
object HowTo : AppDestination {
    override val route: String = "how_to"
    override val title = R.string.app_name
    override val icon: Int = R.drawable.ic_launcher_background

    const val titleArg = "title"
    const val urlArg = "url"
    val routeWithArgs = "${route}/{$titleArg}/{$urlArg}"
    val arguments = listOf(
        navArgument("title") { type = NavType.StringType },
        navArgument("url") { type = NavType.StringType }
    )
}


object RepoDetails : AppDestination {
    override val route = "single_repository"
    override val icon = R.drawable.ic_launcher_background
    override val title = R.string.repository_detail
    const val userNameArg = "user_name"
    const val repoNameArg = "repo_name"
    const val avatarUrlArg = "avatar_url"
    val routeWithArgs = "$route/{$userNameArg}/{$repoNameArg}/{$avatarUrlArg}"
    val arguments = listOf(
        navArgument(userNameArg) { type = NavType.StringType },
        navArgument(repoNameArg) { type = NavType.StringType },
        navArgument(avatarUrlArg) { type = NavType.StringType }
    )
}

val allDestinations = listOf(Summary, AllRepos, RepoDetails)

val bottomNavDestinations = listOf(Summary, AllRepos)