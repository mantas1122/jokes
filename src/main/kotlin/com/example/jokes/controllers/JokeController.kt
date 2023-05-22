package com.example.jokes.controllers

import com.example.jokes.controllers.dto.JokeCategory
import com.example.jokes.controllers.dto.JokeResponse
import com.example.jokes.exceptions.JokeException
import com.example.jokes.services.JokeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

import javax.validation.constraints.Pattern

@RestController
@Tag(name = "Jokes", description = "The Jokes API")
class JokeController(private val jokeService: JokeService) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(JokeException::class)
    fun handleJokeException(e: JokeException): ResponseEntity<Any> {
        logger.error("JokeException occurred", e)
        return ResponseEntity.status(e.status).body(mapOf("error" to e.message))
    }

    @Operation(summary = "Get a random joke")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found a joke", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = JokeResponse::class)
                    )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid input", content = [Content()]),
            ApiResponse(responseCode = "401", description = "No active subscription", content = [Content()]),
            ApiResponse(responseCode = "429", description = "To many requests", content = [Content()]),
            ApiResponse(responseCode = "503", description = "Service unavailable", content = [Content()])]
    )
    @GetMapping("/jokes/random")
    fun random(): ResponseEntity<JokeResponse> {
        val joke = jokeService.getRandomJoke()
        return ResponseEntity.ok(joke)
    }

    /*
        TODO: pagination to ensure that data can be retrieved in manageable chunks. This is important for both
         performance and scalability.

        TODO: Add Spring WebFlux and reactive programming, it can potentially improve performance in certain scenarios.
    */

    @Operation(summary = "Search jokes by term")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found jokes", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = JokeResponse::class)
                    )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid term", content = [Content()]),
            ApiResponse(responseCode = "401", description = "No active subscription", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Jokes not found (not implemented)", content = [Content()]),
            ApiResponse(responseCode = "429", description = "To many requests", content = [Content()]),
            ApiResponse(responseCode = "503", description = "Service unavailable", content = [Content()])]
    )
    @GetMapping("/jokes/search")
    fun searchJoke(
        @RequestParam
        @Parameter(description = "Search term for jokes, only letters are allowed")
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Invalid term")
        term: String
    ): ResponseEntity<List<JokeResponse>> {
        return ResponseEntity.ok(jokeService.searchByTerm(term))
    }


    @Operation(summary = "Get jokes by category")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found jokes", content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = JokeResponse::class)
                    )]
            ),
            ApiResponse(responseCode = "400", description = "Invalid category", content = [Content()]),
            ApiResponse(responseCode = "401", description = "No active subscription", content = [Content()]),
            ApiResponse(responseCode = "429", description = "To many requests", content = [Content()]),
            ApiResponse(responseCode = "503", description = "Service unavailable", content = [Content()])]
    )
    @GetMapping("/jokes/category/{category}")
    fun category(@PathVariable category: JokeCategory): ResponseEntity<List<JokeResponse>> {
        return ResponseEntity.ok(jokeService.searchByCategory(category))
    }
}
