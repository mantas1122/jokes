package com.example.jokes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class JokesApplication
@Suppress("SpreadOperator")
fun main(args: Array<String>) {
	runApplication<JokesApplication>(*args)
}
