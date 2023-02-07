package ru.kettuproj.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.database.data.image.ImageImpl
import ru.kettuproj.database.data.user.UserImpl
import ru.kettuproj.util.ImageUtil
import ru.kettuproj.util.NetUtil
import java.io.File
import java.lang.Exception


fun Application.configureImageRouting() {

    routing {

        route("image"){
            get("data/{name}"){
                //val user = NetUtil.getAuthUser(call) ?: return@get
                val name = call.parameters["name"]

                if(name == null){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                val image = ImageUtil.getImage(name)
                if(image == null){
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respondFile(image)
            }
        }

        authenticate("bearer") {

            route("image") {
                get("my"){
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    val images = ImageImpl().userImages(user.id)
                    call.respond(images)
                }
                post{
                    val user = NetUtil.getAuthUser(call) ?: return@post

                    val multipartData = try {
                        call.receiveMultipart()
                    }catch (e: Exception){
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    val parts = multipartData.readAllParts()


                    if(parts.size != 1){
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    val result = ImageUtil.loadFiles(parts)
                    if(result.isEmpty()){
                        call.respond(HttpStatusCode.InternalServerError)
                        return@post
                    }

                    val imageUUID = result[0]
                    val image = ImageImpl().create(user.id, imageUUID)
                    if(image==null) {
                        call.respond(HttpStatusCode.InternalServerError)
                        return@post
                    }
                    call.respond(image)
                }
                delete {
                    val user = NetUtil.getAuthUser(call) ?: return@delete
                    val uuid = call.request.queryParameters["uuid"]
                    if(uuid==null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@delete
                    }
                    val image = ImageImpl().imageByUUID(uuid)
                    if(image == null){
                        call.respond(HttpStatusCode.NotFound)
                        return@delete
                    }
                    if(image.userId!=user.id){
                        call.respond(HttpStatusCode.Forbidden)
                    }
                    if(ImageImpl().deleteImage(uuid)) {
                        ImageUtil.deleteImage(uuid)
                        call.respond(HttpStatusCode.OK)
                    }
                    else call.respond(HttpStatusCode.InternalServerError)

                }
                get{
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    val uuid = call.request.queryParameters["uuid"]
                    if(uuid==null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }
                    val image = ImageImpl().imageByUUID(uuid)
                    if(image == null){
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(image)
                }
            }
        }
    }
}