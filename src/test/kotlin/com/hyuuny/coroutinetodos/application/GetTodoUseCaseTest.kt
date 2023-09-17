package com.hyuuny.coroutinetodos.application

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

class GetTodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Todo Get") {
        val todo = generateTodo()

        coEvery { repository.findById(any()) } returns todo

        When("Todo를 id로 상세 조회하면") {
            val existingTodo = useCase.get(todo.id)

            Then("존재할 경우 정상 조회된다") {
                existingTodo.id.shouldNotBeNull()
                existingTodo.userId shouldBe todo.userId
                existingTodo.title shouldBe todo.title
                existingTodo.content shouldBe todo.content
                existingTodo.completed shouldBe todo.completed
                existingTodo.createdAt shouldBe todo.createdAt
                existingTodo.updatedAt shouldBe todo.updatedAt
            }

            Then("존재하지 않을 경우 IllegalStateException 예외가 발생한다") {
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
        userId = UserId(UUID.randomUUID()),
        title = "오늘의 할일",
        content = "coroutine 학습",
        completed = false,
        createdAt = now,
        updatedAt = now,
    )
}