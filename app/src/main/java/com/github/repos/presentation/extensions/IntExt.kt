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
fun getAmountAsFloat(str: String): Float { // Bir sayısal diziyi float'a çevirir
    var f = 0f
    if (str.isNotEmpty()) {
        if (str.contains(".")) {
            f = str.toFloat()
        } else if (str.contains(",")) {
            val newStr = str.replace(",", ".")
            f = newStr.toFloat()
        } else {
            f = str.toFloat()
        }
    }
    return f
}