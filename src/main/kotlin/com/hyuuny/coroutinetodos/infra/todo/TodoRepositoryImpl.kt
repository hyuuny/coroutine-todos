package com.hyuuny.coroutinetodos.infra.todo

import com.hyuuny.coroutinetodos.domain.todo.Todo
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.stereotype.Component
import java.util.*

@Component
class TodoRepositoryImpl(
    private val dao: TodoDao,
    private val template: R2dbcEntityTemplate,
) : TodoRepository {

    override suspend fun insert(todo: Todo): Todo = template.insert<Todo>(todo).awaitSingle()

    override suspend fun findById(id: UUID): Todo? = dao.findById(id)

    override suspend fun update(todo: Todo): Todo = template.update<Todo>(todo).awaitSingle()

    override suspend fun delete(id: UUID) {
        template.delete(id).awaitSingle()
    }
}