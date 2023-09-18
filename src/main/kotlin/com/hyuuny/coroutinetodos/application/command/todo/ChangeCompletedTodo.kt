package com.hyuuny.coroutinetodos.application.command.todo

import java.util.*

data class ChangeCompletedTodo(
    val userId: UUID,
    val completed: Boolean,
)
