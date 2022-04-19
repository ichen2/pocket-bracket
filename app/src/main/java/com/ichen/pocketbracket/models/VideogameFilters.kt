package com.ichen.pocketbracket.models

data class VideogameFilter(
    val perPage: Int = 10,
    var page: Int = 1,
    val name: String = "",
)