package com.hyuuny.coroutinetodos.api.handler.request

import java.util.*

data class CreateTodoRequest(
        val userId: UUID,
        val title: String,
        val content: String,
)
