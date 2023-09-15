package com.hyuuny.coroutinetodos.common.constract.todo

import java.util.*

@JvmInline
value class TodoId(val value: UUID) {
    companion object {
        fun new(): TodoId = TodoId(UUID.randomUUID())
    }
}