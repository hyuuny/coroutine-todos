package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
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

    Given("Todo 기능") {
        val todo = generateTodo()

        coEvery { repository.insert(any()) } returns todo
        coEvery { repository.findById(any()) } returns todo

        When("Todo command를 등록하면") {
            val command = CreateTodo(
                userId = todo.userId,
                title = "오늘의 할일",
                content = "coroutine 학습",
            )
            val savedTodo = useCase.create(command)

            Then("command 정보를 기반으로 Todo 등록이 성공한다.") {
                savedTodo.id.shouldNotBeNull()
                savedTodo.userId shouldBe command.userId
                savedTodo.title shouldBe command.title
                savedTodo.content shouldBe command.content
                savedTodo.completed shouldBe todo.completed
                savedTodo.createdAt shouldBe todo.createdAt
                savedTodo.updatedAt shouldBe todo.updatedAt
            }
        }

        When("Todo를 id로 상세 조회하면"){
            val existingTodo = useCase.get(todo.id)

            Then("존재할 경우 정상 조회된다"){
                existingTodo.id.shouldNotBeNull()
                existingTodo.userId shouldBe todo.userId
                existingTodo.title shouldBe todo.title
                existingTodo.content shouldBe todo.content
                existingTodo.completed shouldBe todo.completed
                existingTodo.createdAt shouldBe todo.createdAt
                existingTodo.updatedAt shouldBe todo.updatedAt
            }

            Then("존재하지 않을 경우 IllegalStateException 예외가 발생한다"){
                coEvery { repository.findById(any()) } returns null

                val id = UUID.randomUUID()
                val exception = shouldThrow<IllegalStateException> {
                    useCase.get(id)
                }
                exception.message shouldBe "${id}번 Todo를 찾을 수 없습니다."
            }
        }

    }

})

private fun generateTodo(): Todo {
    val now = LocalDateTime.now()
    return Todo(
        id = TodoId.new().value,
        userId = UUID.randomUUID(),
        title = "오늘의 할일",
        content = "coroutine 학습",
        completed = false,
        createdAt = now,
        updatedAt = now,
    )
}