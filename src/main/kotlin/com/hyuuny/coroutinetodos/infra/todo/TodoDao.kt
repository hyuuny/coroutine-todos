package com.hyuuny.coroutinetodos.infra.todo

import com.hyuuny.coroutinetodos.domain.todo.Todo
import kotlinx.coroutines.flow.Flow
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TodoDao : CoroutineCrudRepository<Todo, UUID> {

    suspend fun findAllByUserIdOrderByCreatedAtDesc(userId: UUID): Flow<Todo>

}