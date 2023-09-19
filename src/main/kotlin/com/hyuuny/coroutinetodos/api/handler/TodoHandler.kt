package com.hyuuny.coroutinetodos.api.handler

import com.hyuuny.coroutinetodos.api.handler.request.CompletedTodoRequest
import com.hyuuny.coroutinetodos.api.handler.request.CreateTodoRequest
import com.hyuuny.coroutinetodos.api.handler.request.UpdateTodoRequest
import com.hyuuny.coroutinetodos.api.handler.response.AllTodoResponse
import com.hyuuny.coroutinetodos.api.handler.response.TodoResponse
import com.hyuuny.coroutinetodos.application.TodoUseCase
import com.hyuuny.coroutinetodos.application.command.todo.ChangeCompletedTodo
import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.application.command.todo.UpdateTodo
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import java.util.*

@Component
class TodoHandler(
        private val useCase: TodoUseCase,
) {

    @Bean
    fun getAllTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            GET("/todos/all/{userId}") { request ->
                val userId = request.pathVariable("userId")
                val todos = useCase.getAllTodo(UserId(UUID.fromString(userId)))

                ok().bodyValueAndAwait(AllTodoResponse(
                        todos = todos.map(::toResponse),
                        count = todos.size,
                ))
            }
        }
    }

    @Bean
    fun getTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            GET("/todos/{id}") { request ->
                val todoId = request.pathVariable("id")
                val todo = useCase.get(toUuid(todoId))

                ok().bodyValueAndAwait(toResponse(todo))
            }
        }
    }

    @Bean
    fun createTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            POST("/todos") { request ->
                val body = request.awaitBody<CreateTodoRequest>()
                val command = CreateTodo(
                        userId = body.userId,
                        title = body.title,
                        content = body.content,
                )
                val savedTodo = useCase.create(command)

                ok().bodyValueAndAwait(toResponse(savedTodo))
            }
        }
    }

    @Bean
    fun updateTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            PUT("/todos/{id}") { request ->
                val body = request.awaitBody<UpdateTodoRequest>()
                val todoId = request.pathVariable("id")
                val command = UpdateTodo(
                        userId = body.userId,
                        title = body.title,
                        content = body.content
                )
                val updatedTodo = useCase.update(toUuid(todoId), command)

                ok().bodyValueAndAwait(toResponse(updatedTodo))
            }
        }
    }

    @Bean
    fun deleteTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            DELETE("/todos/{id}") { request ->
                val todoId = request.pathVariable("id")
                useCase.delete(toUuid(todoId))

                noContent().buildAndAwait()
            }
        }
    }

    @Bean
    fun completeTodo(): RouterFunction<ServerResponse> {
        return coRouter {
            PATCH("/todos/{id}/complete") { request ->
                val body = request.awaitBody<CompletedTodoRequest>()
                val todoId = request.pathVariable("id")
                val command = ChangeCompletedTodo(
                        userId = body.userId,
                        completed = body.completed,
                )
                val completedTodo = useCase.completed(toUuid(todoId), command)

                ok().bodyValueAndAwait(toResponse(completedTodo))
            }
        }
    }

    private fun toResponse(completedTodo: Todo) = TodoResponse(
            id = completedTodo.id,
            userId = completedTodo.userId.value,
            title = completedTodo.title,
            content = completedTodo.content,
            completed = completedTodo.completed,
            createdAt = completedTodo.createdAt,
            updatedAt = completedTodo.updatedAt,
    )

    private fun toUuid(todoId: String): UUID = UUID.fromString(todoId)

}