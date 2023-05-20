package com.example.jokes

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["classpath:features"],
    glue = ["com.example.jokes"],
    plugin = ["pretty"]
)
class CucumberIntegrationTest
