package ru.kettuproj.database.data.image

import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.kettuproj.database.DatabaseFactory
import ru.kettuproj.database.IMAGE
import ru.kettuproj.model.Image
import ru.kettuproj.model.imageResult

class ImageImpl : ImageInterface {

    override suspend fun create(userID: Int, uuid: String): Image? = DatabaseFactory.dbQuery {
        val statement = IMAGE
            .insert {
                it[IMAGE.USER_ID] = userID
                it[IMAGE.ID] = uuid
                it[IMAGE.CREATED] = System.currentTimeMillis()
            }
        statement.resultedValues?.singleOrNull()?.let(::imageResult)
    }

    override suspend fun userImages(userID: Int): List<Image> = DatabaseFactory.dbQuery {
        IMAGE
            .select { IMAGE.USER_ID eq userID }
            .map(::imageResult)
    }

    override suspend fun imageByUUID(uuid: String): Image? = DatabaseFactory.dbQuery {
        IMAGE
            .select { IMAGE.ID eq uuid }
            .singleOrNull()
           ?.let(::imageResult)
    }

    override suspend fun deleteImage(uuid: String): Boolean = DatabaseFactory.dbQuery {
        val count = IMAGE
            .deleteWhere { IMAGE.ID eq uuid }
        count != 0
    }

}