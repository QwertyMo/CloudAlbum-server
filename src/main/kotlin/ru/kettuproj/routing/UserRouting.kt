package ru.kettuproj.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.util.NetUtil


fun Application.configureUserRouting() {

    routing {
        authenticate("bearer") {
            route("user") {
                get {
                    val param = call.request.queryParameters
                    val login = param["user"]
                    val user = NetUtil.getUserByLogin(call, login) ?: return@get
                    call.respond(user)
                }
                get("me"){
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    call.respond(user)
                }
            }
        }
    }
}