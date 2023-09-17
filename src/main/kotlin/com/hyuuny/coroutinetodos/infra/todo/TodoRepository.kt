package com.hyuuny.coroutinetodos.infra.todo

import com.hyuuny.coroutinetodos.domain.todo.Todo
import java.util.*

interface TodoRepository {

    suspend fun insert(todo: Todo): Todo

    suspend fun findById(id: UUID): Todo?

    suspend fun update(todo: Todo): Todo

    suspend fun delete(id: UUID)
}