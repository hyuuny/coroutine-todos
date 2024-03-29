package com.hyuuny.coroutinetodos.domain.todo

import com.hyuuny.coroutinetodos.application.command.todo.ChangeCompletedTodo
import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.application.command.todo.UpdateTodo
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import com.hyuuny.coroutinetodos.common.constract.user.UserId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("todos")
class Todo(
    @Id val id: UUID,
    val userId: UserId,
    title: String,
    content: String,
    completed: Boolean = false,
    val createdAt: LocalDateTime,
    updatedAt: LocalDateTime,
) {

    var title: String = title
        private set

    var content: String = content
        private set

    var completed: Boolean = completed
        private set

    var updatedAt: LocalDateTime = updatedAt
        private set

    companion object {
        fun create(command: CreateTodo): Todo {
            val now = LocalDateTime.now()
            return Todo(
                id = TodoId.new().value,
                userId = UserId(command.userId),
                title = command.title,
                content = command.content,
                completed = false,
                createdAt = now,
                updatedAt = now,
            )
        }
    }

    fun handle(command: UpdateTodo) {
        title = command.title
        content = command.content
        updatedAt = LocalDateTime.now()
    }

    fun handle(command: ChangeCompletedTodo) {
        this.completed = command.completed
        updatedAt = LocalDateTime.now()
    }

    fun checkUser(userId: UUID): Todo {
        if (this.userId.value != userId) throw IllegalStateException("회원이 일치하지 않습니다.")
        return this
    }

    fun checkCompleted() {
        if (this.completed) throw IllegalStateException("이미 완료된 할일입니다.")
    }

}