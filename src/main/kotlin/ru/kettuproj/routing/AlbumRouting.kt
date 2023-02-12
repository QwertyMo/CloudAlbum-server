package ru.kettuproj.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.database.data.album.AlbumImpl
import ru.kettuproj.util.NetUtil

fun Application.configureAlbumRouting() {

    routing {
        authenticate("bearer") {
            route("album") {
                post{
                    val user = NetUtil.getAuthUser(call) ?: return@post
                    val name  = NetUtil.getParamOrResponse(call, "name") ?: return@post

                    val result = AlbumImpl().create(user.id, name)
                    if(result==null){
                        call.respond(HttpStatusCode.InternalServerError)
                        return@post
                    }
                    call.respond(result)
                }
                get{
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    val id  = (NetUtil.getParamOrResponse(call, "id") ?: return@get).toInt()

                    if(!AlbumImpl().canUserAccess(user.id, id)) {
                        call.respond(HttpStatusCode.Unauthorized)
                        return@get
                    }

                    val album = AlbumImpl().getAlbum(id)
                    if(album == null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    call.respond(album)
                }
                get("my"){
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    call.respond(AlbumImpl().getUserAlbums(user.id))
                }
            }
        }
    }
}