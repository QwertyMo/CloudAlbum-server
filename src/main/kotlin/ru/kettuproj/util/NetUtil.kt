package ru.kettuproj.util

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import ru.kettuproj.database.data.user.UserImpl
import ru.kettuproj.model.User

object NetUtil {
    suspend fun getAuthUser(call: ApplicationCall): User?{
        val id = call.principal<UserIdPrincipal>()?.name?.toInt()
        if(id == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return null
        }

        val user = UserImpl().getUser(id)
        if(user == null) {
            call.respond(HttpStatusCode.Unauthorized)
            return null
        }
        return user
    }

    suspend fun getUserByLogin(call: ApplicationCall, login: String?): User?{
        if (login.isNullOrBlank()) {
            call.respond(HttpStatusCode.NotFound)
            return null
        }

        val user = UserImpl().getUser(login)
        if (user == null) {
            call.respond(HttpStatusCode.NotFound)
            return null
        }
        return user
    }

    suspend fun getParamOrResponse(call: ApplicationCall, name: String): String?{
        val param = call.request.queryParameters[name] as String
        if(param==null) call.respond(HttpStatusCode.BadRequest)
        return param
    }
}