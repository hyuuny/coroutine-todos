package com.hyuuny.coroutinetodos.api.handler.request

import java.util.*

data class UpdateTodoRequest(
        val userId: UUID,
        val title: String,
        val content: String,
)
