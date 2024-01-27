package com.example.deamhome.domain.model

data class UserProfile(
    val name: String,
    val profile: String,
) {
    companion object {
        val EMPTY = UserProfile("", "")
    }
}
