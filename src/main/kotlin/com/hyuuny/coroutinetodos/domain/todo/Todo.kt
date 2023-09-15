package com.hyuuny.coroutinetodos.domain.todo

import com.hyuuny.coroutinetodos.application.command.todo.CreateTodo
import com.hyuuny.coroutinetodos.common.constract.todo.TodoId
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime
import java.util.*

@Table("todos")
class Todo(
    @Id val id: UUID,
    val userId: UUID,
    val title: String,
    val content: String,
    val completed: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    companion object {
        fun create(command: CreateTodo): Todo {
            val now = LocalDateTime.now()
            return Todo(
                id = TodoId.new().value,
                userId = command.userId,
                title = command.title,
                content = command.content,
                completed = false,
                createdAt = now,
                updatedAt = now,
            )
        }
    }

}