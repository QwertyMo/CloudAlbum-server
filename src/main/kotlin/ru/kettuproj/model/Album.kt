package ru.kettuproj.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ru.kettuproj.database.ALBUM

fun albumResult(row: ResultRow) = Album(
    row[ALBUM.ID],
    row[ALBUM.NAME],
    row[ALBUM.CREATOR_ID]
)

@Serializable
data class Album (
    val id: Int,
    val name: String,
    val creatorId: Int
)