package com.hyuuny.coroutinetodos.api.handler.request

import java.util.*

data class CompletedTodoRequest(
        val userId: UUID,
        val completed: Boolean,
)
