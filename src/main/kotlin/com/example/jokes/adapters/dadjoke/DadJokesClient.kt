package com.example.jokes.adapters.dadjoke

import com.example.jokes.exceptions.JokeException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus.*
import org.springframework.stereotype.Component

@Component
class DadJokesClient {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${dad-jokes-client.url}")
    private lateinit var baseUrl: String

    @Value("\${dad-jokes-client.headers.x-rapidapi-key}")
    private lateinit var apiKey: String

    @Value("\${dad-jokes-client.headers.x-rapidapi-host}")
    private lateinit var apiHost: String

    private val client = OkHttpClient()

    fun getRandomJoke(): DadJoke {
        logger.info("Getting random joke")
        val jsonData = callApi("random/joke")
        return decodeResponse(jsonData).body!!.first()
    }

    fun searchByTerm(term: String): List<DadJoke> {
        logger.info("Searching joke by term: $term")
        val jsonData = callApi("joke/search?term=$term")
        return decodeResponse(jsonData).body!!
    }

    fun searchByCategory(category: String): List<DadJoke> {
        logger.info("Searching joke by category: $category")
        val jsonData = callApi("joke/type/$category")
        return decodeResponse(jsonData).body!!
    }

    private fun validateResponse(response: Response) {
        when (val statusCode = response.code) {
            TOO_MANY_REQUESTS.value() -> {
                val message = "Rate limit exceeded"
                logger.error(message)
                throw JokeException(statusCode, message)
            }

            UNAUTHORIZED.value() -> {
                val message = "No subscription. Check if your provided key supports this endpoint"
                logger.error(message)
                throw JokeException(statusCode, message)
            }

            else -> {
                if (!response.isSuccessful || response.body == null) {
                    logger.error("Joke API returned unsuccessful response: ${response.body}")
                    throw JokeException(SERVICE_UNAVAILABLE.value(), "Joke API returned unsuccessful response")
                }
            }
        }
    }

    private fun callApi(path: String): String {
        val request = Request.Builder()
            .url("$baseUrl/$path")
            .get()
            .addHeader("X-RapidAPI-Key", apiKey)
            .addHeader("X-RapidAPI-Host", apiHost)
            .build()
        val response = client.newCall(request).execute()
        validateResponse(response)
        return response.body!!.string()
    }


    private fun decodeResponse(jsonData: String): DadJokeResponse {
        val jokeResponse = Json { ignoreUnknownKeys = true }.decodeFromString<DadJokeResponse>(jsonData)
        if (jokeResponse.success) {
            return jokeResponse
        } else {
            logger.error("Joke API returned unsuccessful response: $jsonData")
            throw JokeException(SERVICE_UNAVAILABLE.value(), "Joke API returned unsuccessful response")
        }
    }
}
