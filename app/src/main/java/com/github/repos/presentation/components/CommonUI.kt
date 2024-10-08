package com.github.repos.presentation.components

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.bumptech.glide.Glide
import com.github.repos.presentation.extensions.toPx
import com.github.repos.presentation.navigation.AppDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: AppDestination,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = Modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        },
        actions = {
            /*todo drawer icon*/
        }
    )
}

@Composable
fun BottomNavigationBar(
    allScreens: List<AppDestination>,
    onTabSelected: (AppDestination) -> Unit,
    currentScreen: AppDestination,
    navController: NavHostController
) {
    BottomAppBar(
        actions = {
            Row(
                Modifier.selectableGroup()
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                allScreens.forEach { screen ->
                    Text(
                        text = stringResource(screen.title).uppercase(),
                        fontWeight = if (screen == currentScreen) MaterialTheme.typography.titleLarge.fontWeight else MaterialTheme.typography.titleMedium.fontWeight,
                        color = if (screen == currentScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.clickable {
                            onTabSelected(screen)
                        },
                    )
                }
            }
        },
    )
}

@Composable
fun NavigationDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
            /* Drawer content */
                drawerContent()
            }
        },
    ) {
        Scaffold(
        ) { contentPadding ->
            Surface(Modifier.padding(contentPadding)) {
                content()
            }
        }
    }
}

@Composable
fun LoadImageFromUrl(url: String) {
    AndroidView(factory = { context ->
        ImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(32.dp.toPx(context), 32.dp.toPx(context))
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }, update = { imageView ->
        Glide.with(imageView.context)
            .load(url)
            .override(32.dp.toPx(imageView.context), 32.dp.toPx(imageView.context))
            .into(imageView)
    })
}
