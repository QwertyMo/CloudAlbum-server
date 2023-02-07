package ru.kettuproj.database.data.album


import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import ru.kettuproj.database.*
import ru.kettuproj.model.*

class AlbumImpl : AlbumInterface{
    override suspend fun create(userID: Int, name: String): Album? = DatabaseFactory.dbQuery {
        val statement = ALBUM
            .insert {
                it[ALBUM.CREATOR_ID] = userID
                it[ALBUM.NAME] = name
            }
        statement.resultedValues?.singleOrNull()?.let(::albumResult)
    }

    override suspend fun getUserAlbums(userID: Int): List<Album> = DatabaseFactory.dbQuery {
        ALBUM
            .leftJoin(ALBUM_USER)
            .select { (ALBUM_USER.USER_ID eq userID) or (ALBUM.CREATOR_ID eq userID) }
            .map(::albumResult)
    }

    override suspend fun addUserToAlbum(albumID: Int, userID: Int): Boolean = DatabaseFactory.dbQuery {
        ALBUM_USER
            .insert {
                it[ALBUM_USER.USER_ID] = userID
                it[ALBUM_USER.ALBUM_ID] = albumID
            }
        true
    }

    override suspend fun albumUsers(albumID: Int): List<User> = DatabaseFactory.dbQuery {
        val res1 = ALBUM_USER
            .innerJoin(USER)
            .select { ALBUM_USER.ALBUM_ID eq albumID }
            .map(::userResult)
        val res2 = ALBUM
            .innerJoin(USER)
            .select { ALBUM.ID eq albumID  }
            .singleOrNull()
            ?.let(::userResult)
        if(res2==null) res1
        else {
            val result = res1.toMutableList()
            result.add(res2)
            result
        }
    }

    override suspend fun addImageToAlbum(albumID: Int, imageID: String): Boolean = DatabaseFactory.dbQuery {
        ALBUM_IMAGE
            .insert {
                it[ALBUM_IMAGE.ALBUM_ID] = albumID
                it[ALBUM_IMAGE.IMAGE_ID] = imageID
            }
        true
    }
}