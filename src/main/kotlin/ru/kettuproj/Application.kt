package ru.kettuproj

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import ru.kettuproj.database.DatabaseFactory
import ru.kettuproj.database.data.album.AlbumImpl
import ru.kettuproj.database.data.user.UserImpl
import ru.kettuproj.routing.configureAuthRouting
import ru.kettuproj.routing.configureImageRouting
import ru.kettuproj.routing.configureUserRouting
import ru.kettuproj.util.PasswordUtil

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation){
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
    DatabaseFactory.init()

        runBlocking {

        }

    configureAuthRouting()
    configureUserRouting()
    configureImageRouting()

}
