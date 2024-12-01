package com.themrfill.iloveittest.utils

fun String.tidy(): String {
    val newString = this
        .replace("&quot;", "\"")
        .replace("&amp;", "&")
    return newString
}