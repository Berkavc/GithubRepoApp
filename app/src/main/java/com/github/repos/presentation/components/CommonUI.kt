package com.github.repos.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.github.repos.AppDestination
import com.github.repos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    currentScreen: AppDestination,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit
) {
    /*todo
    *  summary ekrani icin drawer cikacak
    * diger ekranlar icin back button*/

    TopAppBar(
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
    /*todo*/
    Surface(
        Modifier
            .height(69.dp) /*todo adaptive UI*/
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Row(
            Modifier.selectableGroup(),
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
//                Image(
//                    painter = painterResource(screen.icon),
//                    contentDescription = null,
//                    colorFilter = ColorFilter.tint( if (screen == currentScreen) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface),
//                    modifier = Modifier.clickable {
//                        onTabSelected(screen)
//                    },
//                )
            }
        }
    }
}
