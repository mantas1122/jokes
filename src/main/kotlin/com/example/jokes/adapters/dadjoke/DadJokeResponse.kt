package com.example.jokes.adapters.dadjoke

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DadJokeResponse(
    val success: Boolean,
    val body: List<DadJoke>? = null
)

@Serializable
data class DadJoke(
    @SerialName("_id")
    val id: String,
    val type: String,
    val setup: String,
    val punchline: String
)
