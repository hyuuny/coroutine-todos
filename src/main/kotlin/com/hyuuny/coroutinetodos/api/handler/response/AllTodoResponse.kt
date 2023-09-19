package com.hyuuny.coroutinetodos.api.handler.response

data class AllTodoResponse(
        val todos: List<TodoResponse>,
        val count: Int,
)
