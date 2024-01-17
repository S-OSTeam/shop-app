package com.example.deamhome.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val accessToken: String,
    val refreshToken: String,
) {
    companion object {
        const val EMPTY = ""
        val EMPTY_TOKEN = Token(EMPTY, EMPTY)
    }
}
