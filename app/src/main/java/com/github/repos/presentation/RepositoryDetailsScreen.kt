package com.github.repos.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.repos.R
import com.github.repos.data.model.ResponseState
import com.github.repos.presentation.components.LoadImageFromUrl
import com.github.repos.presentation.repodetails.RepositoryDetailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepositoryDetailsScreen(
    navController: NavHostController = rememberNavController(),
    userName: String,
    repoName: String,
    avatarUrl: String,
    viewModel: RepositoryDetailsViewModel = hiltViewModel()
) {
    val repositoryDetails by viewModel.repositoryDetails.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.getRepositoryDetails(userName, repoName)
    }
    Column(modifier = Modifier.fillMaxWidth()) {
//        TopAppBar(
//            colors = TopAppBarDefaults.topAppBarColors(
//                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
//                titleContentColor = Color.White,
//            ),
//            title = {
//                Text(
//                    stringResource(id = R.string.repository_detail),
//                    maxLines = 1,
//                    textAlign = TextAlign.Start,
//                    color = Color.White,
//                    fontSize = 20.sp,
//                    overflow = TextOverflow.Ellipsis,
//                    fontWeight = FontWeight.Bold,
//                )
//            },
//            navigationIcon = {
//                IconButton(onClick = { onBackPressed() }) {
//                    Icon(Icons.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
//                }
//            }
//        )

        when (val state = repositoryDetails) {
            is ResponseState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is ResponseState.Success -> {
                val repoDetails = state.data!!
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box() {
                        LoadImageFromUrl(avatarUrl)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.repo_name),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "${repoDetails.name}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.owner),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "${repoDetails.owner.login}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.forks_count),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "${repoDetails.forksCount}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.language),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "${repoDetails.language ?: "N/A"}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.default_branch),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.DarkGray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Text(
                        text = "${repoDetails.defaultBranch ?: ""}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            is ResponseState.Error -> {
                Toast.makeText(
                    LocalContext.current,
                    state.message ?: "Connection Error",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }
}