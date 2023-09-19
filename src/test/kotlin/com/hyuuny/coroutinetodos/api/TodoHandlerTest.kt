package com.hyuuny.coroutinetodos.api

import com.hyuuny.coroutinetodos.api.handler.TodoHandler
import com.hyuuny.coroutinetodos.application.TodoUseCase
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockKExtension::class)
@WebFluxTest(TodoHandler::class)
class TodoHandlerTest {

    @MockkBean
    private lateinit var todoUseCase: TodoUseCase

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `TODO 목록 조회`() {
        val userId = UserId.new()
        val todos = generateTodos(userId)
        coEvery { todoUseCase.getAllTodo(userId) }.returns(todos)

        webTestClient.get().uri("/todos/all/{userId}", userId.value.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(::println)
                .jsonPath("$.todos[0].id").isEqualTo(todos[0].id.toString())
                .jsonPath("$.todos[0].userId").isEqualTo(todos[0].userId.value.toString())
                .jsonPath("$.todos[0].title").isEqualTo(todos[0].title)
                .jsonPath("$.todos[0].content").isEqualTo(todos[0].content)
                .jsonPath("$.todos[0].completed").isEqualTo(todos[0].completed)
                .jsonPath("$.todos[10].id").isEqualTo(todos[10].id.toString())
                .jsonPath("$.todos[10].userId").isEqualTo(todos[10].userId.value.toString())
                .jsonPath("$.todos[10].title").isEqualTo(todos[10].title)
                .jsonPath("$.todos[10].content").isEqualTo(todos[10].content)
                .jsonPath("$.todos[10].completed").isEqualTo(todos[10].completed)
                .jsonPath("$.count").isEqualTo(todos.size)
    }

    @Test
    fun `TODO 상세 조회`() {
        val todoId = TodoId.new().value
        val todo = generateTodo(todoId)
        coEvery { todoUseCase.get(any()) }.returns(todo)

        webTestClient.get().uri("/todos/{id}", todoId.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(::println)
                .jsonPath("$.id").isEqualTo(todoId.toString())
                .jsonPath("$.userId").isEqualTo(todo.userId.value.toString())
                .jsonPath("$.title").isEqualTo(todo.title)
                .jsonPath("$.content").isEqualTo(todo.content)
                .jsonPath("$.completed").isEqualTo(todo.completed)
    }

    @Test
    fun `TODO 상세 조회시 없는 TODO면 예외가 발생한다`() {
        webTestClient.get().uri("/todos/{id}", TodoId.new().value)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError
    }

    @Test
    fun `TODO 등록`() {
        val todoId = TodoId.new().value
        val todo = generateTodo(todoId)
        coEvery { todoUseCase.create(any()) }.returns(todo)

        webTestClient.post().uri("/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(todo))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(::println)
                .jsonPath("$.id").isEqualTo(todoId.toString())
                .jsonPath("$.userId").isEqualTo(todo.userId.value.toString())
                .jsonPath("$.title").isEqualTo(todo.title)
                .jsonPath("$.content").isEqualTo(todo.content)
                .jsonPath("$.completed").isEqualTo(todo.completed)
    }

    @Test
    fun `TODO 수정`() {
        val todoId = TodoId.new().value
        val todo = Todo(
                id = todoId,
                userId = UserId.new(),
                title = "내일의 할일",
                content = "coroutine 학습",
                completed = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
        coEvery { todoUseCase.update(any(), any()) }.returns(todo)

        webTestClient.put().uri("/todos/{id}", todoId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(todo))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(::println)
                .jsonPath("$.id").isEqualTo(todoId.toString())
                .jsonPath("$.userId").isEqualTo(todo.userId.value.toString())
                .jsonPath("$.title").isEqualTo(todo.title)
                .jsonPath("$.content").isEqualTo(todo.content)
                .jsonPath("$.completed").isEqualTo(todo.completed)
    }

    @Test
    fun `TODO 삭제`() {
        val todoId = TodoId.new().value
        coEvery { todoUseCase.delete(any()) }.returns(Unit)

        webTestClient.delete().uri("/todos/{id}", todoId.toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent
                .expectBody()
                .consumeWith(::println)
    }

    @Test
    fun `TODO 완료`() {
        val todoId = TodoId.new().value
        val todo = Todo(
                id = todoId,
                userId = UserId.new(),
                title = "오늘의 할일",
                content = "webflux 학습",
                completed = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
        )
        coEvery { todoUseCase.completed(any(), any()) }.returns(todo)

        webTestClient.patch().uri("/todos/{id}/complete", todoId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(todo))
                .exchange()
                .expectStatus().isOk
                .expectBody()
                .consumeWith(::println)
                .jsonPath("$.id").isEqualTo(todoId.toString())
                .jsonPath("$.userId").isEqualTo(todo.userId.value.toString())
                .jsonPath("$.title").isEqualTo(todo.title)
                .jsonPath("$.content").isEqualTo(todo.content)
                .jsonPath("$.completed").isEqualTo(true)
    }

    private fun generateTodo(todoId: UUID): Todo {
        val now = LocalDateTime.now()
        return Todo(
                id = todoId,
                userId = UserId.new(),
                title = "오늘의 할일",
                content = "webflux 학습",
                completed = false,
                createdAt = now,
                updatedAt = now,
        )
    }

    private fun generateTodos(userId: UserId): List<Todo> {
        val todos = mutableListOf<Todo>()
        for (i in 1..11) {
            val now = LocalDateTime.now()
            todos.add(
                    Todo(
                            id = TodoId.new().value,
                            userId = userId,
                            title = "${i}번째 오늘의 할일",
                            content = "${i}번째 학습",
                            completed = false,
                            createdAt = now,
                            updatedAt = now,
                    )
            )
        }
        return todos.sortedByDescending { it.createdAt }
    }
}