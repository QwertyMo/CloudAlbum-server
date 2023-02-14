package ru.kettuproj.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow
import ru.kettuproj.database.INVITE

fun inviteResult(row: ResultRow) = Invite(
    row[INVITE.CREATOR_ID],
    row[INVITE.USER_ID],
    row[INVITE.ALBUM_ID]
)
@Serializable
data class Invite(
    val creatorId: Int,
    val userId: Int,
    val albumId: Int
)
