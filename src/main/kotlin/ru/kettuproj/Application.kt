package ru.kettuproj

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import ru.kettuproj.database.DatabaseFactory
import ru.kettuproj.routing.configureAlbumRouting
import ru.kettuproj.routing.configureAuthRouting
import ru.kettuproj.routing.configureImageRouting
import ru.kettuproj.routing.configureUserRouting
import ru.kettuproj.util.ImageUtil
import java.awt.GraphicsEnvironment

private val logger = LoggerFactory.getLogger(Application::class.java)
fun main() {
    ImageUtil.scanImageFormats()
    System.setProperty("java.awt.headless", "false")
    if(!GraphicsEnvironment.isHeadless()) logger.info("Running in headless mode")
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
    configureAlbumRouting()

}
