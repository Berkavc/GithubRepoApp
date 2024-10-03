import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.github.repos.presentation.mainactivity.MainViewModel
import com.github.repos.R
import com.github.repos.data.model.AllRepositories
import com.github.repos.data.model.ResponseState
import com.github.repos.presentation.navigation.navigateDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllRepositoriesScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
//    val viewModel = hiltViewModel<MainViewModel>()
    val context = LocalContext.current
    val allRepositories by viewModel.allRepositories.observeAsState()

    Column {
        when (val state = allRepositories) {
            is ResponseState.Loading -> {
                CircularProgressIndicator(
                    Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                )
            }

            is ResponseState.Success -> {
                val repoList = state.data
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(repoList!!.size) { index ->
                        RepoItemView(repo = repoList[index]) { username, repoName, avatarUrl ->
                            // Navigate to the repository details
                            navController.navigateDetails(username, repoName, avatarUrl)
                        }
                    }
                }
            }

            is ResponseState.Error -> {
                Toast.makeText(
                    context,
                    context.getString(R.string.connection_error),
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }

        LaunchedEffect(Unit) {
            viewModel.getAllRepositories()
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
                .width(32.dp)
                .height(32.dp)
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

@Composable
fun RepoItemView(repo: AllRepositories, onClick: (String, String, String) -> Unit) {
    Column(modifier = Modifier.clickable {
        onClick(
            repo.owner.login,
            repo.name,
            repo.owner.avatarUrl
        )
    }) {
        Row(
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            LoadImageFromUrl(repo.owner.avatarUrl)
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .fillMaxHeight()
            ) {
                Text(
                    text = repo.name,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = repo.owner.login,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxWidth()

                )

            }
        }
        Divider(
            color = Color.LightGray,
            thickness = 1.dp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    }
}