package ru.kettuproj.database

import org.jetbrains.exposed.sql.Table

object IMAGE: Table(){
    val USER_ID = reference("USER_ID", USER.ID)
    val ID = varchar("ID", 255).uniqueIndex()
    val CREATED = long("CREATED")
    val ALBUM_ID = reference("ALBUM_ID", ALBUM.ID).nullable()

    override val primaryKey = PrimaryKey(ID)
}

object TOKEN: Table(){
    val USER_ID = reference("USER_ID", USER.ID)
    val VALUE = varchar("VALUE", 255).uniqueIndex()
}

object USER: Table(){
    val ID = integer("ID").autoIncrement().uniqueIndex()
    val LOGIN = varchar("LOGIN", 255).uniqueIndex()
    val PASSWORD = varchar("PASSWORD", 255)
    val CREATED = long("CREATED")

    override val primaryKey = PrimaryKey(ID)
}

object ALBUM: Table(){
    val ID = integer("ID").autoIncrement().uniqueIndex()
    val CREATOR_ID = reference("CREATOR_ID", USER.ID)
    val NAME = varchar("NAME", 255)

    override val primaryKey = PrimaryKey(ID)

}

object ATTRIBUTE: Table(){
    val ID = integer("ID").autoIncrement().uniqueIndex()
    val NAME = varchar("NAME", 255)

    override val primaryKey = PrimaryKey(ID)
}

object ROLE: Table(){
    val ID = integer("ID").autoIncrement().uniqueIndex()
    val ALBUM_ID = reference("ALBUM_ID", ALBUM.ID)
    val NAME = varchar("NAME", 255)

    override val primaryKey = PrimaryKey(ID)
}

object ROLE_ATTRIBUTE: Table(){
    val ROLE_ID = reference("ROLE_ID", ROLE.ID)
    val ATTRIBUTE_ID = reference("ATTRIBUTE_ID", ATTRIBUTE.ID)
}

object ALBUM_USER: Table(){
    val ALBUM_ID = reference("ALBUM_ID", ALBUM.ID)
    val USER_ID = reference("USER_ID", USER.ID)
    val ROLE_ID = reference("ROLE_ID", ROLE.ID).nullable()
}