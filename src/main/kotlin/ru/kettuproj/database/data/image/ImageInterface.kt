package ru.kettuproj.database.data.image

import ru.kettuproj.model.Image

interface ImageInterface {
    suspend fun create(userID: Int, path: String): Image?
    suspend fun userImages(userID: Int): List<Image>
    suspend fun imageByUUID(uuid: String): Image?
    suspend fun deleteImage(uuid: String): Boolean
    suspend fun create(userID: Int, uuid: String, album: Int): Image?
    suspend fun albumImages(album: Int): List<Image>
}