package com.hyuuny.coroutinetodos.application

import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import com.hyuuny.coroutinetodos.domain.todo.Todo
import com.hyuuny.coroutinetodos.infra.todo.TodoRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDateTime

class GetAllTodoUseCaseTest : BehaviorSpec({

    val repository = mockk<TodoRepository>()
    val useCase = TodoUseCase(repository)

    Given("Get All Todo") {
        val userId = UserId.new()
        val todos = generateTodos(userId)

        coEvery { repository.findAllByUserIdOrderByCreatedAtDesc(userId) } returns todos

        When("Todo 목록을 조회하면") {
            Then("Todo 생성일을 기준으로 내림차순 정렬되어 조회된다.") {
                val allTodo = useCase.getAllTodo(userId)

                allTodo.forEach {todo ->
                    todo.id.shouldNotBeNull()
                    todo.userId shouldBe userId
                }
                allTodo[0].title shouldBe "11번째 오늘의 할일"
                allTodo[0].content shouldBe "11번째 학습"
                allTodo[10].title shouldBe "1번째 오늘의 할일"
                allTodo[10].content shouldBe "1번째 학습"
                allTodo.size shouldBe todos.size
            }

            Then("Todo를 등록한 적이 없으면 빈 목록이 조회된다.") {
                coEvery { repository.findAllByUserIdOrderByCreatedAtDesc(userId) } returns emptyList()
                val allTodo = useCase.getAllTodo(userId)

                allTodo shouldBe emptyList()
            }
        }
    }
})

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