package com.github.repos.presentation.allrepos

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import com.github.repos.R
import com.github.repos.databinding.FragmentAllRepositoriesBinding
import com.github.repos.domain.model.AllRepositories
import com.github.repos.domain.model.ResponseState
import com.github.repos.domain.util.ReposItemDecoration
import com.github.repos.presentation.allrepos.adapter.AllRepositoriesAdapter
import com.google.android.material.progressindicator.CircularProgressIndicator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RepoListScreenActivity : ComponentActivity() {
    private val allRepoListViewModel: AllRepositoriesViewModel by viewModels()
    private lateinit var binding: FragmentAllRepositoriesBinding
    private lateinit var navController: NavController
    private var allRepoList: List<AllRepositories>? = null
    private lateinit var allReposAdapter: AllRepositoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RepoScreen(viewModel = allRepoListViewModel, navController = navController)
        }
    }
}

@Composable
fun RepoScreen(viewModel: AllRepositoriesViewModel, navController: NavController) {
    val context = LocalContext.current
    val allRepositories by viewModel.allRepositories.observeAsState()

    when (val state = allRepositories) {
        null -> {
            CircularProgressIndicator()
        }
        is ResponseState.Loading -> {
            CircularProgressIndicator()
        }
        is ResponseState.Success -> {
            val repoList = state.data // Burada repoList'i alıyoruz

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(repoList!!.size) { index ->
                    RepoItemView(repo = repoList[index]) { username, repoName ->
                        val fieldBundle = bundleOf("user_name" to username, "repo_name" to repoName)
                        navController.navigate(R.id.action_fragmentRepos_to_repositoryDetailFragment, fieldBundle)
                    }
                }
            }
        }
        is ResponseState.Error -> {
            Toast.makeText(context, context.getString(R.string.connection_error), Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllRepositories()
    }
}


@Composable
fun RepoItemView(repo: AllRepositories, onClick: (String, String) -> Unit) {
    Text(
        text = repo.name,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(repo.fullName, repo.name) }
            .padding(16.dp)
    )
}
