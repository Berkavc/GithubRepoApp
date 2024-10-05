package com.github.repos.presentation.extensions

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.preference.PreferenceManager
import com.github.repos.Const
import com.github.repos.R
import com.github.repos.presentation.navigation.HowTo


object ViewExt {
    fun checkIconAvailability(activity: Activity, image: Int): Boolean {
        val resources: Resources = activity.resources
        var drawable: Drawable? = null
        return try {
            drawable = resources.getDrawable(image)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun hideKeyboard(activity: Activity) { // Eğer bir Activity'de bir odaklanılan alan varsa, bu odaktan klavyeyi gizler
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun hideKeyboard(et: EditText) { // EditText üzerinde klavyeyi gizlemek veya kapalıysa yeniden açmak için kullanılır
        val imm = et.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    fun openFailedLoginPopup(activity: Activity?) { // Başarısız giriş sonrası bir popup açmak için kullanılır
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(activity!!)
        val editor = sharedPref.edit()
        editor.putString(
            Const.IS_FAILED_LOGIN_CLOSED,
            "NO"
        )
        editor.commit()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun openWebSheet(
        url: String?,
        title: String?,
        onDismiss: () -> Unit
    ) {
        val sheetStateHelp = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            sheetState = sheetStateHelp,
            containerColor = colorResource(id = R.color.white),
            contentColor = colorResource(id = R.color.white),
            onDismissRequest = { onDismiss() },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.height(0.dp))
                    Text(
                        text = title ?: "",
                        style = TextStyle(
                            fontSize = 18.sp,
                            lineHeight = 21.6.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.black),
                            letterSpacing = 0.2.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(modifier = Modifier.fillMaxWidth(0.915f))
                    Spacer(modifier = Modifier.height(16.dp))

                    AndroidView(factory = {
                        WebView(it).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            webViewClient = WebViewClient()
                            settings.builtInZoomControls = false
                            loadUrl(url ?: "")
                        }
                    }, update = {
                        it.loadUrl(url ?: "")
                    })
                }
            }
        }
    }


    @JvmStatic
    fun openWebView(navController: NavController, redirectUrl: String?, title: String?) {
        val encodedAvatarUrl = Uri.encode(redirectUrl)
        navController.navigate("${HowTo.route}/${title ?: ""}/${encodedAvatarUrl ?: ""}")
//      navController.navigateWebView(title, redirectUrl)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PageHeader(
        title: String,
        onBackPressed: () -> Unit
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.transparent),
                titleContentColor = Color.Black,
            ),
            title = {
                Text(
                    title,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.black),
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                )
            },
            navigationIcon = {
                IconButton(onClick = { onBackPressed() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_simple),
                        contentDescription = "Back"
                    )
                }
            }
        )
    }

    @Composable
    fun CustomPopup( // Alert tipini belirleyecek Image, Title, Message / Annotated String, Filled Button ve Outlined Button bulunan popup
        title: String,
        annotatedMessage: AnnotatedString? = null,
        message: String? = null,
        showAnnotatedMessage: Boolean,  // Boolean for showing annotatedMessage or message
        painter: Painter,
        onConfirmClick: () -> Unit,
        confirmText: String,
        onDismiss: () -> Unit,
        dismissText: String,
        showTwoButtons: Boolean  // Two-button control
    ) {
        require(!(annotatedMessage == null && message == null)) {
            "Either annotatedMessage or normalMessage must be provided"
        }

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = colorResource(id = R.color.white),
                            shape = RoundedCornerShape(size = 12.dp)
                        )
                        .padding(start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp),
                    shape = RoundedCornerShape(size = 12.dp),
                    colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.white))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(32.dp, 40.dp, 32.dp, 32.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        Image(
                            painter = painter,
                            contentDescription = "",
                            modifier = Modifier.size(136.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.black),
                                textAlign = TextAlign.Center
                            ),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (showAnnotatedMessage) {
                            annotatedMessage?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        } else {
                            message?.let {
                                Text(
                                    text = it,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                            modifier = Modifier
                                .fillMaxWidth(0.99f)
                                .padding(start = 0.dp, end = 0.dp, top = 0.dp, bottom = 0.dp)
                                .clickable { /*onConfirmClick()*/ }
                        ) {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = colorResource(id = R.color.teal_200),
                                    contentColor = colorResource(id = R.color.turquoise)
                                ),
                                onClick = { onConfirmClick() },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .clickable { onConfirmClick() }
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth()
                                    .padding(22.5.dp, 0.dp, 22.5.dp, 0.dp),
                                contentPadding = PaddingValues(20.dp, 13.dp)
                            ) {
                                Text(
                                    text = confirmText,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        lineHeight = 19.2.sp,
                                        fontWeight = FontWeight(700),
                                        color = colorResource(id = R.color.white),
                                        letterSpacing = 0.2.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }

                            if (showTwoButtons) {
                                Spacer(modifier = Modifier.height(24.dp))
                                OutlinedButton(
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = colorResource(id = R.color.transparent),
                                        contentColor = colorResource(id = R.color.turquoise),
                                        disabledContentColor = colorResource(id = R.color.turquoise),
                                        disabledContainerColor = colorResource(id = R.color.transparent)
                                    ),
                                    onClick = { onDismiss() },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        colorResource(id = R.color.turquoise)
                                    ),
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .fillMaxWidth()
                                        .padding(22.5.dp, 0.dp, 22.5.dp, 0.dp),
                                    contentPadding = PaddingValues(20.dp, 13.dp)
                                ) {
                                    Text(
                                        text = dismissText,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(0.dp, 0.dp, 0.dp, 0.dp),
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            lineHeight = 19.2.sp,
                                            fontWeight = FontWeight(700),
                                            color = colorResource(id = R.color.turquoise),
                                            letterSpacing = 0.2.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}