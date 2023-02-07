package ru.kettuproj.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import ru.kettuproj.database.IMAGE

fun imageResult(row: ResultRow) = Image(
    row[IMAGE.USER_ID],
    row[IMAGE.ID],
    row[IMAGE.CREATED]
)

@Serializable
data class Image(
    val userId: Int,
    val uuid: String,
    val created: Long
)
