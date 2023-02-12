package ru.kettuproj.database.data.album

import ru.kettuproj.model.Album
import ru.kettuproj.model.User

interface AlbumInterface {
    suspend fun create(userID: Int, name: String): Album?
    suspend fun addUserToAlbum(albumID: Int, userID: Int): Boolean
    suspend fun albumUsers(albumID: Int): List<User>
    suspend fun addImageToAlbum(albumID: Int, imageID: String): Boolean
    suspend fun getUserAlbums(userID: Int): List<Album>
    suspend fun getAlbum(albumID: Int): Album?
    suspend fun canUserUpload(userID: Int, albumID: Int): Boolean
    suspend fun canUserAccess(userID: Int, albumID: Int): Boolean
}