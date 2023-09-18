package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.ChangeCompletedTodo
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.hyuuny.coroutinetodos.infra.todo.TodoRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDateTime

class ChangeCompletedTodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Todo 완료") {
        val userId = UserId.new()
        val todo = generateTodo(userId)

        When("Todo ChangeCompleted Command를 등록하면") {
            Then("completed가 true면 할일 완료로 수정된다.") {
                coEvery { repository.findById(any()) } returns todo
                coEvery { repository.update(any()) } returns updatedTodo(userId)

                val command = ChangeCompletedTodo(
                    userId = userId.value,
                    completed = true,
                )
                val updatedTodo = useCase.completed(todo.id, command)
                updatedTodo.id.shouldNotBeNull()
                updatedTodo.userId.value shouldBe command.userId
                updatedTodo.completed shouldBe command.completed
                updatedTodo.updatedAt shouldNotBe updatedTodo.createdAt
            }

            Then("userId가 일치하지 않으면 IllegalStateException 예외가 발생한다.") {
                coEvery { repository.findById(any()) } returns todo
                coEvery { repository.update(any()) } returns updatedTodo(userId)

                val exception = shouldThrow<IllegalStateException> {
                    useCase.completed(
                        todo.id, ChangeCompletedTodo(
                            userId = UserId.new().value,
                            completed = true,
                        )
                    )
                }
                exception.message shouldBe "회원이 일치하지 않습니다."
            }

            Then("존재하지 않는 Todo일 경우 IllegalStateException 예외가 발생한다") {
                coEvery { repository.findById(any()) } returns null

                val id = TodoId.new().value
                val exception = shouldThrow<IllegalStateException> {
                    useCase.completed(
                        id, ChangeCompletedTodo(
                            userId = userId.value,
                            completed = true,
                        )
                    )
                }
                exception.message shouldBe "${id}번 Todo를 찾을 수 없습니다."
            }

            Then("이미 완료된 상태면 IllegalStateException 예외가 발생한다.") {
                val now = LocalDateTime.now()
                coEvery { repository.findById(any()) } returns Todo(
                    id = TodoId.new().value,
                    userId = userId,
                    title = "오늘의 할일",
                    content = "coroutine 학습",
                    completed = true,
                    createdAt = now,
                    updatedAt = now,
                )

                val exception = shouldThrow<IllegalStateException> {
                    useCase.completed(
                        todo.id, ChangeCompletedTodo(
                            userId = userId.value,
                            completed = true,
                        )
                    )
                }
                exception.message shouldBe "이미 완료된 할일입니다."
            }
        }
    }
})

private fun generateTodo(userId: UserId): Todo {
    val now = LocalDateTime.now()
    return Todo(
        id = TodoId.new().value,
        userId = userId,
        title = "오늘의 할일",
        content = "coroutine 학습",
        completed = false,
        createdAt = now,
        updatedAt = now,
    )
}

private fun updatedTodo(userId: UserId): Todo {
    return Todo(
        id = TodoId.new().value,
        userId = userId,
        title = "오늘의 할일",
        content = "coroutine 학습",
        completed = true,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}