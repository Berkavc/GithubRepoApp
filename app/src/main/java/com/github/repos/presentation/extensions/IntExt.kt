package com.github.repos.presentation.extensions

import android.content.Context
import android.util.TypedValue
import androidx.compose.ui.unit.Dp

fun Dp.toPx(context: Context): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.value,
        context.resources.displayMetrics
    ).toInt()
}