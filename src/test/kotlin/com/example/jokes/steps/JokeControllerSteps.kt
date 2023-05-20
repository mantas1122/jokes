package com.example.jokes.steps

import com.example.jokes.controllers.dto.JokeResponse
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import io.cucumber.java.After
import io.cucumber.java.Before
import io.cucumber.java.en.*
import org.junit.Assert
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClientResponseException
import org.springframework.web.client.RestTemplate

class JokeControllerSteps {

    private val restTemplate: RestTemplate = RestTemplate()
    private var jokeResponse: JokeResponse? = null
    private var exception: RestClientResponseException? = null

    private val wireMockServer = WireMockServer(8081)

    @LocalServerPort
    private var port: Int = 0

    @Before
    fun setup() {
        wireMockServer.start()
        configureFor("localhost", wireMockServer.port())
    }

    @After
    fun teardown() {
        wireMockServer.stop()
    }

    @Given("a joke service is available")
    fun setupJokeService() {
        stubFor(
            get(urlEqualTo("/random/joke"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(ClassPathResource("responses/random.json").file.readText())
                        .withStatus(HttpStatus.OK.value())
                )
        )
    }

    @Given("the joke service is not available")
    fun setupUnavailableJokeService() {
        stubFor(
            get(urlEqualTo("/random/joke"))
                .willReturn(aResponse().withStatus(HttpStatus.TOO_MANY_REQUESTS.value()))
        )
    }

    @Given("the joke service is returning an unsuccessful response")
    fun setupUnsuccessfulJokeService() {
        stubFor(
            get(urlEqualTo("/random/joke"))
                .willReturn(
                    aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""{ "success": false }""")
                        .withStatus(HttpStatus.SERVICE_UNAVAILABLE.value())
                )
        )
    }

    @When("the client requests a random joke")
    fun clientRequestsARandomJoke() {
        try {
            jokeResponse = restTemplate.getForObject("http://localhost:$port/jokes/random", JokeResponse::class.java)
        } catch (ex: RestClientResponseException) {
            exception = ex
        }
    }

    @Then("the service should return a joke")
    fun serviceShouldReturnAJoke() {
        Assert.assertNotNull(jokeResponse)
        Assert.assertTrue(jokeResponse?.punchline == "Rol.exeSorry for the trash pun, thought about it while" +
                " walking in front of rolex")
    }

    @Then("the service should return a 'rate limit exceeded' error")
    fun serviceShouldReturnRateLimitError() {
        Assert.assertNotNull(exception)
        Assert.assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), exception?.rawStatusCode)
    }

    @Then("the service should return a 'Joke API returned unsuccessful response' error")
    fun serviceShouldReturnUnsuccessfulResponseError() {
        Assert.assertNotNull(exception)
        Assert.assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), exception?.rawStatusCode)
    }
}
