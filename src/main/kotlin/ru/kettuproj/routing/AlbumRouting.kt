package ru.kettuproj.routing

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.util.NetUtil

fun Application.configureAlbumRouting() {

    routing {
        authenticate("bearer") {
            route("album") {
                post("create") {

                }
            }
        }
    }
}