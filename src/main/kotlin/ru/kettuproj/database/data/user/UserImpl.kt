package ru.kettuproj.database.data.user

import org.jetbrains.exposed.sql.*
import ru.kettuproj.database.DatabaseFactory
import ru.kettuproj.database.TOKEN
import ru.kettuproj.database.USER
import ru.kettuproj.model.User
import ru.kettuproj.model.userResult
import ru.kettuproj.util.PasswordUtil
import java.util.UUID

class UserImpl : UserInterface {

    override suspend fun create(login: String, password: String): User? {
        if(isLoginExists(login)) return null
        val user = register(login) ?: return null
        updatePass(user, password)
        return user
    }

    override suspend fun updatePass(login: String, password: String): Boolean = DatabaseFactory.dbQuery {
        val user = getUser(login)
        if(user==null) false
        else{
            USER.update({ USER.LOGIN.lowerCase() eq login.lowercase() }) {
                it[PASSWORD] = PasswordUtil.generateHash(password, user.id.toString())
            }
            true
        }
    }

    override suspend fun updatePass(user: User, password: String): Boolean = DatabaseFactory.dbQuery {
        USER.update({ USER.LOGIN.lowerCase() eq user.login.lowercase() }) {
            it[PASSWORD] = PasswordUtil.generateHash(password, user.id.toString())
        }
        true
    }

    override suspend fun getUser(id: Int): User? = DatabaseFactory.dbQuery {
        USER
            .select { USER.ID eq id }
            .map(::userResult)
            .singleOrNull()
    }

    override suspend fun getUser(login: String): User? = DatabaseFactory.dbQuery {
        USER
            .select { USER.LOGIN.lowerCase() eq login.lowercase() }
            .map(::userResult)
            .singleOrNull()
    }

    override suspend fun allUsers(): List<User> = DatabaseFactory.dbQuery {
        USER
            .selectAll()
            .map(::userResult)
    }

    override suspend fun login(login: String, password: String): String? = DatabaseFactory.dbQuery{
        val user = getUser(login)
        if(user == null) null
        else if(!checkUserPass(user, password)) null
        else{
            val token = PasswordUtil.generateHash(UUID.randomUUID().toString(), user.id.toString())
            val statement = TOKEN.insert {
                it[USER_ID] = user.id
                it[VALUE] = token
            }
            if(statement.resultedValues.isNullOrEmpty()) null
            else token
        }
    }

    private suspend fun isLoginExists(login: String): Boolean = DatabaseFactory.dbQuery {
        getUser(login)!=null
    }

    private suspend fun register(login: String): User? = DatabaseFactory.dbQuery{
        val statement = USER.insert {
            it[LOGIN] = login
            it[PASSWORD] = UUID.randomUUID().toString()
            it[CREATED] = System.currentTimeMillis()
        }
        statement.resultedValues?.singleOrNull()?.let(::userResult)
    }

    private suspend fun checkUserPass(user: User, password: String): Boolean = DatabaseFactory.dbQuery{
        val userPass = USER
            .select{USER.LOGIN.lowerCase() eq user.login.lowercase()}
            .map { it[USER.PASSWORD] }
            .singleOrNull()
        if(userPass == null) false
        else PasswordUtil.generateHash(password, user.id.toString()) == userPass

    }

    override suspend fun getUserByToken(token: String): User? = DatabaseFactory.dbQuery {
        USER.innerJoin(TOKEN)
            .select { TOKEN.VALUE eq token }
            .map(::userResult)
            .singleOrNull()
    }


}