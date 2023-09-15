package com.hyuuny.coroutinetodos.infra.todo

import com.hyuuny.coroutinetodos.domain.todo.Todo
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.util.*

interface TodoDao : CoroutineCrudRepository<Todo, UUID>