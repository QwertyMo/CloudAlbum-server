package ru.kettuproj.util

import ru.kettuproj.model.Album
import ru.kettuproj.model.AlbumRights
import ru.kettuproj.model.User

object AlbumUtil {
    suspend fun getRights(user: User, album: Album): AlbumRights{

        //TODO: Role checking
        val rights = AlbumRights()

        if(user.id == album.creatorId){
            rights.canAddImage = true
            rights.canInvite = true
            rights.canRename = true
            rights.canChangeIcon = true
            rights.canSeeImage = true
            rights.canDeleteImage = true
            return rights
        }

        rights.canSeeImage = true
        return rights

    }
}