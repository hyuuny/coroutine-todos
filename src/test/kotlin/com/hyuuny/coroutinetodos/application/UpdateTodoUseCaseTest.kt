package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.application.command.todo.UpdateTodo
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

class UpdateTodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Todo Update") {
        val userId = UserId.new()
        val todo = generateTodo(userId)

        coEvery { repository.findById(any()) } returns todo
        coEvery { repository.update(any()) } returns updatedTodo(userId)

        When("Todo Update Command를 등록하면") {
            val command = UpdateTodo(
                userId = userId.value,
                title = "내일의 할일",
                content = "webflux 학습",
            )
            val updatedTodo = useCase.update(todo.id, command)

            Then("Update Command 정보를 기반으로 Todo 내용이 수정된다.") {
                updatedTodo.id.shouldNotBeNull()
                updatedTodo.userId.value shouldBe command.userId
                updatedTodo.title shouldBe command.title
                updatedTodo.content shouldBe command.content
                updatedTodo.updatedAt shouldNotBe updatedTodo.createdAt
            }

            Then("userId가 일치하지 않으면 IllegalStateException 예외가 발생한다.") {
                val exception = shouldThrow<IllegalStateException> {
                    useCase.update(
                        todo.id, UpdateTodo(
                            userId = UserId.new().value,
                            title = "내일의 할일",
                            content = "webflux 학습"
                        )
                    )
                }
                exception.message shouldBe "회원이 일치하지 않습니다."
            }

            Then("존재하지 않는 Todo일 경우 IllegalStateException 예외가 발생한다") {
                coEvery { repository.findById(any()) } returns null

                val id = TodoId.new().value
                val exception = shouldThrow<IllegalStateException> {
                    useCase.update(
                        id, UpdateTodo(
                            userId = todo.userId.value,
                            title = "내일의 할일",
                            content = "webflux 학습"
                        )
                    )
                }
                exception.message shouldBe "${id}번 Todo를 찾을 수 없습니다."
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
        title = "내일의 할일",
        content = "webflux 학습",
        completed = false,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}