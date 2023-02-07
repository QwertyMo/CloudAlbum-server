package ru.kettuproj.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ru.kettuproj.database.USER

fun userResult(row: ResultRow) = User(
    row[USER.ID],
    row[USER.LOGIN],
    row[USER.CREATED]
)

@Serializable
data class User(
    val id: Int,
    val login: String,
    val created: Long
)