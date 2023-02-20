package ru.kettuproj.database.data.invite

import ru.kettuproj.model.Invite

interface InviteInterface {
    suspend fun inviteUser(crearorID: Int, userID: Int, albumID: Int): Invite?
    suspend fun getInvites(userID: Int): List<Invite>
    suspend fun acceptInvite(userID: Int, albumID: Int): Boolean
    suspend fun declineInvite(userID: Int, albumID: Int): Boolean
}