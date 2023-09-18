package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.ChangeCompletedTodo
import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.application.command.todo.UpdateTodo
import com.hyuuny.coroutinetodos.common.constract.user.UserId
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

    suspend fun get(id: UUID): Todo = loadTodo(id)

    suspend fun update(
        id: UUID,
        command: UpdateTodo
    ): Todo {
        val todo = loadTodo(id)
        todo.checkUser(command.userId)
        todo.handle(command)
        return todoRepository.update(todo)
    }

    private suspend fun loadTodo(id: UUID) =
        todoRepository.findById(id) ?: throw IllegalStateException("${id}번 Todo를 찾을 수 없습니다.")

    suspend fun delete(id: UUID) {
        val todo = loadTodo(id)
        todoRepository.delete(todo.id)
    }

    suspend fun completed(
        id: UUID,
        command: ChangeCompletedTodo,
    ): Todo {
        val todo = loadTodo(id)
        todo.checkUser(command.userId).checkCompleted()
        todo.handle(command)
        return todoRepository.update(todo)
    }

    suspend fun getAllTodo(userId: UserId): List<Todo> = todoRepository.findAllByUserIdOrderByCreatedAtDesc(userId)

}