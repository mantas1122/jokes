package com.example.jokes

import io.cucumber.spring.CucumberContextConfiguration
import org.junit.Before
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootContextLoader
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@CucumberContextConfiguration
@ContextConfiguration(classes = [JokesApplication::class], loader = SpringBootContextLoader::class)

class SpringIntegrationTest {
    private val logger = LoggerFactory.getLogger(this::class.java)
    /**
   * Need this method so the cucumber will recognize this class as glue and load spring context configuration
   */
    @Before
    fun setUp() {
        logger.info("- Spring Context Initialized For Executing Cucumber Tests -")
    }
}
