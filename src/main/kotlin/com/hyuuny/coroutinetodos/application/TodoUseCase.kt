package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.hyuuny.coroutinetodos.infra.todo.TodoRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TodoUseCase(
    private val todoRepository: TodoRepository,
) {

    suspend fun create(command: CreateTodo): Todo {
        val newTodo = Todo.create(command)
        return todoRepository.insert(newTodo)
    }

    suspend fun get(id: UUID): Todo =
        todoRepository.findById(id) ?: throw IllegalStateException("${id}번 Todo를 찾을 수 없습니다.")

}