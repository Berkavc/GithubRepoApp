package com.github.repos.presentation.repodetails

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.repos.R
import com.github.repos.domain.model.ResponseState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@AndroidEntryPoint
class RepoDetailActivity : ComponentActivity() {
    private lateinit var username: String
    private lateinit var repoName: String
    private lateinit var avatarUrl: String
    private val repoDetailsViewModel: RepositoryDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        username = intent.getStringExtra("user_name") ?: "Unknown"
        repoName = intent.getStringExtra("repo_name") ?: "Unknown"
        avatarUrl = intent.getStringExtra("avatar_url") ?: "Unknown"

        setContent {
            RepoDetailScreen(
                username = username,
                repoName = repoName,
                avatarUrl = avatarUrl,
                viewModel = repoDetailsViewModel,
                onBackPressed = { finish() }
            )
        }
    }
}

@Composable
fun LoadImageFromUrl(url: String) {
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(url) {
        bitmap = loadImage(url)
    }

    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(), contentDescription = "Avatar",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )
    }
}

suspend fun loadImage(url: String): Bitmap? {
    return withContext(Dispatchers.IO) {
        try {
            val inputStream = URL(url).openStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            null
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepoDetailScreen(
    username: String,
    repoName: String,
    avatarUrl: String,
    viewModel: RepositoryDetailsViewModel,
    onBackPressed: () -> Unit
) {
    val repositoryDetails by viewModel.repositoryDetails.observeAsState()
    Column(modifier = Modifier.fillMaxWidth()) {

        LaunchedEffect(Unit) {
            viewModel.getRepositoryDetails(username, repoName)
        }
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                titleContentColor = Color.White,
            ),
            title = {
                Text(
                    stringResource(id = R.string.repository_detail),
                    maxLines = 1,
                    textAlign = TextAlign.Start,
                    color = Color.White,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Geri", tint = Color.White)
                }
            }
        )

        when (val state = repositoryDetails) {
            null, is ResponseState.Loading -> {
                CircularProgressIndicator()
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
        }
    }
}


