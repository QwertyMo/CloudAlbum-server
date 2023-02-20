package ru.kettuproj.database.data.invite

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.kettuproj.database.DatabaseFactory
import ru.kettuproj.database.INVITE
import ru.kettuproj.database.data.album.AlbumImpl
import ru.kettuproj.model.Invite
import ru.kettuproj.model.inviteResult

class InviteImpl : InviteInterface {

    override suspend fun inviteUser(crearorID: Int, userID: Int, albumID: Int): Invite? = DatabaseFactory.dbQuery{
        val statement = INVITE
            .insert {
                it[INVITE.CREATOR_ID] = crearorID
                it[INVITE.USER_ID] = userID
                it[INVITE.ALBUM_ID] = albumID
            }
        statement.resultedValues?.singleOrNull()?.let(::inviteResult)
    }

    override suspend fun getInvites(userID: Int): List<Invite> = DatabaseFactory.dbQuery{
        INVITE
            .select { INVITE.USER_ID eq userID}
            .map(::inviteResult)
    }

    override suspend fun acceptInvite(userID: Int, albumID: Int): Boolean = DatabaseFactory.dbQuery {
        INVITE
            .deleteWhere{(INVITE.USER_ID eq userID) and (INVITE.ALBUM_ID eq albumID)}
        AlbumImpl().addUserToAlbum(albumID, userID)
    }

    override suspend fun declineInvite(userID: Int, albumID: Int): Boolean = DatabaseFactory.dbQuery {
        INVITE
            .deleteWhere{(INVITE.USER_ID eq userID) and (INVITE.ALBUM_ID eq albumID)}
        true
    }

}