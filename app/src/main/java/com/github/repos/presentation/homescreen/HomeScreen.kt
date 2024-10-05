package com.github.repos.presentation.homescreen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.navigation.NavController
import com.github.repos.R
import com.github.repos.presentation.extensions.ViewExt.CustomPopup
import com.github.repos.presentation.extensions.ViewExt.openWebSheet

@Composable
fun CustomPopupScreen(navController: NavController) {
    var showPopup by rememberSaveable { mutableStateOf(false) }

    Button(onClick = { showPopup = true }) {
        Text("Show Popup")
    }

    if (showPopup) {
        CustomPopup(
            title = "Title",
//            message = "Message",
//            showAnnotatedMessage = false,
            showAnnotatedMessage = true,
            annotatedMessage = AnnotatedString("Annotated Message"),
            painter = painterResource(id = R.drawable.rounded_edge),
            onConfirmClick = {
                showPopup = false
            },
            confirmText = "OK",
            onDismiss = {
                showPopup = false
            },
            dismissText = "Cancel",
            showTwoButtons = true
        )
    }
}

@Composable
fun OpenWebSheetScreen(navController: NavController) {
    var showWebSheet by rememberSaveable { mutableStateOf(false) }

    Button(onClick = {
        showWebSheet = true
    }) {
        Text(text = "Go to HowToScreen")
    }

    if (showWebSheet) {
        openWebSheet(
            url = "https://developer.android.com/?hl=tr",
            title = "Android Developer",
            onDismiss = { showWebSheet = false }
        )
    }
}