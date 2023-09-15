package com.hyuuny.coroutinetodos

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineTodosApplication

fun main(args: Array<String>) {
    runApplication<CoroutineTodosApplication>(*args)
}
