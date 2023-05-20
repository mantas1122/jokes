package com.example.jokes.exceptions

class JokeException(val status: Int, override val message: String): RuntimeException()

