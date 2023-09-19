package com.hyuuny.coroutinetodos.api.handler.response

import java.time.LocalDateTime
import java.util.*

data class TodoResponse(
        val id: UUID,
        val userId: UUID,
        val title: String,
        val content: String,
        val completed: Boolean,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
)
