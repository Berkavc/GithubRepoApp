package com.github.repos.presentation.extensions

fun getAmountAsFloat(str: String): Float {
    var value = 0f
    if (str.isNotEmpty()) {
        value = if (str.contains(".")) {
            str.toFloat()
        } else if (str.contains(",")) {
            val newStr = str.replace(",", ".")
            newStr.toFloat()
        } else {
            str.toFloat()
        }
    }
    return value
}