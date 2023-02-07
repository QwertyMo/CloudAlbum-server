package ru.kettuproj.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.database.data.user.UserImpl
import ru.kettuproj.model.Token
fun Application.configureAuthRouting() {

    authentication {
        bearer("bearer") {
            realm = "Access to the '/' path"
            authenticate { tokenCredential ->
                val token = tokenCredential.token
                val user = UserImpl().getUserByToken(token)
                if(user!=null) UserIdPrincipal(user.id.toString())
                else null
            }
        }
    }

    routing {
        post("/login"){
            val login = call.request.queryParameters["login"]    as String
            val pass  = call.request.queryParameters["password"] as String

            val token = UserImpl().login(login, pass)
            if(token!=null) call.respond(Token(token))
            else call.respond(HttpStatusCode.Unauthorized)
        }
    }
}