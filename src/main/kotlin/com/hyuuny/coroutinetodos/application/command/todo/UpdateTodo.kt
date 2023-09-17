package com.hyuuny.coroutinetodos.application.command.todo

import java.util.*

data class UpdateTodo(
    val userId: UUID,
    val title: String,
    val content: String,
)
