package ru.kettuproj.database.data.user

import ru.kettuproj.model.User

interface UserInterface {
    suspend fun create(login: String, password: String): User?
    suspend fun updatePass(login: String, password: String): Boolean
    suspend fun getUser(id: Int): User?
    suspend fun getUser(login: String): User?
    suspend fun allUsers(): List<User>
    suspend fun login(login: String, password: String): String?
    suspend fun getUserByToken(token: String): User?
    suspend fun updatePass(user: User, password: String): Boolean
}