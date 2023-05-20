package com.example.jokes.services

import com.example.jokes.adapters.dadjoke.DadJokesClient
import com.example.jokes.controllers.dto.JokeCategory
import com.example.jokes.controllers.dto.JokeResponse
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class JokeService(private val dadJokesClient: DadJokesClient) {

    fun getRandomJoke(): JokeResponse {
        val dadJoke = dadJokesClient.getRandomJoke()
        return JokeResponse(
            id = dadJoke.id,
            category = dadJoke.type,
            setup = dadJoke.setup,
            punchline = dadJoke.punchline
        )
    }

    /* Added Cashing to increase performance and reduce the usage of external api.
     It might make sense to use Redis or Hazelcast in case we will run multiple pods and will want share
     cache between pods
    */
    @Cacheable("jokesByTerm")
    fun searchByTerm(term: String): List<JokeResponse> {
        val dadJokes = dadJokesClient.searchByTerm(term)

        return dadJokes.map { joke ->
            JokeResponse(
                id = joke.id,
                category = joke.type,
                setup = joke.setup,
                punchline = joke.punchline
            )
        }
    }

    @Cacheable("jokesByCategory")
    fun searchByCategory(category: JokeCategory): List<JokeResponse> {
        val dadJokes = dadJokesClient.searchByCategory(category.value)

        return dadJokes.map { joke ->
            JokeResponse(
                id = joke.id,
                category = joke.type,
                setup = joke.setup,
                punchline = joke.punchline
            )
        }
    }
}
