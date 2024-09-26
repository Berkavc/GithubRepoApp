package com.github.repos

import com.github.repos.domain.model.AllRepositories

sealed interface AppDestination {
    val route : String
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
    override val icon: Int =  R.drawable.ic_launcher_background
}


//data object SingleRepository : AppDestination { /*todo */
//    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't
//    // part of the RallyTabRow selection
//    override val icon: Int? = null
//    override val route = "single_reporistory"
//    override val title: Int
//        get() = TODO("Not yet implemented")
//    const val accountTypeArg = "account_type"
//    val routeWithArgs = "$route/{$accountTypeArg}"
//    val arguments = listOf(
//        navArgument(accountTypeArg) { type = NavType.StringType }
//    )
//    val deepLinks = listOf(
//        navDeepLink { uriPattern = "rally://$route/{$accountTypeArg}" }
//    )
//}

val allDestinations = listOf(Summary, AllRepos)

val bottomNavDestinations = listOf(Summary, AllRepos)

//todo drawer