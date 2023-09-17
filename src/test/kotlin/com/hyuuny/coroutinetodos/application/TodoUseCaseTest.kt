package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.hyuuny.coroutinetodos.infra.todo.TodoRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDateTime
import java.util.*

class TodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Todo Create") {
        val todo = generateTodo()

        coEvery { repository.insert(any()) } returns todo

        When("Todo command를 등록하면") {
            val command = CreateTodo(
                userId = todo.userId.value,
                title = "오늘의 할일",
                content = "coroutine 학습",
            )
            val savedTodo = useCase.create(command)

            Then("command 정보를 기반으로 Todo 등록이 성공한다.") {
                savedTodo.id.shouldNotBeNull()
                savedTodo.userId.value shouldBe command.userId
                savedTodo.title shouldBe command.title
                savedTodo.content shouldBe command.content
                savedTodo.completed shouldBe todo.completed
                savedTodo.createdAt shouldBe todo.createdAt
                savedTodo.updatedAt shouldBe todo.updatedAt
            }
        }
    }
})

private fun generateTodo(): Todo {
    val now = LocalDateTime.now()
    return Todo(
        id = TodoId.new().value,
        userId = UserId(UUID.randomUUID()),
        title = "오늘의 할일",
        content = "coroutine 학습",
        completed = false,
        createdAt = now,
        updatedAt = now,
    )
}