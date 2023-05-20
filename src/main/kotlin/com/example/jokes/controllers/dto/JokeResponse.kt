package com.example.jokes.controllers.dto

data class JokeResponse(
    val id: String,
    val category: String,
    val setup: String,
    val punchline: String
)
