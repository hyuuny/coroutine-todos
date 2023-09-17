package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.hyuuny.coroutinetodos.infra.todo.TodoRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import java.util.*

class DeleteTodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Todo Delete") {
        val todo = generateTodo()

        coEvery { repository.findById(any()) } returns todo
        coEvery { repository.delete(any()) } returns Unit

        When("Todo를 삭제하면") {
            useCase.delete(todo.id)

            Then("존재할 경우 정상 삭제된다") {
                coVerify(exactly = 1) { repository.delete(any()) }
            }

            Then("존재하지 않을 경우 IllegalStateException 예외가 발생한다") {
                coEvery { repository.findById(any()) } returns null

                val id = UUID.randomUUID()
                val exception = shouldThrow<IllegalStateException> {
                    useCase.delete(id)
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