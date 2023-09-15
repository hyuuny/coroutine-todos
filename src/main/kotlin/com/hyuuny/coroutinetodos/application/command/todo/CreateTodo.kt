package com.hyuuny.coroutinetodos.application.command.todo

import java.util.*

data class CreateTodo(
    val userId: UUID,
    val title: String,
    val content: String,
    val completed: Boolean = false,
)