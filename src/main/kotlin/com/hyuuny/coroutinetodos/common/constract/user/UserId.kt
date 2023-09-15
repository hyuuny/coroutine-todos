package com.hyuuny.coroutinetodos.common.constract.user

import java.util.*

@JvmInline
value class UserId(val value: UUID) {
    companion object {
        fun new(): UserId = UserId(UUID.randomUUID())
    }
}