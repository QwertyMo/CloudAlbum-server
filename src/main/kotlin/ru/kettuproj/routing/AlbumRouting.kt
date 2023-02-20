package ru.kettuproj.routing

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kettuproj.database.data.album.AlbumImpl
import ru.kettuproj.database.data.invite.InviteImpl
import ru.kettuproj.database.data.user.UserImpl
import ru.kettuproj.model.Invite
import ru.kettuproj.util.AlbumUtil
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
                    val id  = (NetUtil.getParamOrResponse(call, "albumID") ?: return@get).toInt()

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
                post("invite") {
                    val creator = NetUtil.getAuthUser(call) ?: return@post
                    val userID = (NetUtil.getParamOrResponse(call, "userID") ?: return@post).toInt()
                    val albumID = (NetUtil.getParamOrResponse(call, "albumID") ?: return@post).toInt()

                    val album = AlbumImpl().getAlbum(albumID)
                    val user = UserImpl().getUser(userID)

                    if(album == null || user == null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    if(!AlbumImpl().canUserInvite(creator.id, album.id)) {
                        call.respond(HttpStatusCode.Forbidden)
                        return@post
                    }

                    if(AlbumImpl().getUserAlbums(user.id).find { it.id == album.id } != null){
                        call.respond(HttpStatusCode.Conflict)
                        return@post
                    }

                    if(InviteImpl().getInvites(user.id).find { it.albumId == album.id } != null){
                        call.respond(HttpStatusCode.Conflict)
                        return@post
                    }

                    val res = InviteImpl().inviteUser(creator.id, user.id, album.id)
                    if(res==null) call.respond(HttpStatusCode.InternalServerError)
                    else call.respond(HttpStatusCode.OK)
                    return@post
                }
                get("invite"){
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    call.respond(InviteImpl().getInvites(user.id))
                    return@get
                }
                post("accept"){
                    val user = NetUtil.getAuthUser(call) ?: return@post
                    val albumID = (NetUtil.getParamOrResponse(call, "albumID") ?: return@post).toInt()

                    val album = AlbumImpl().getAlbum(albumID)

                    if(album == null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@post
                    }

                    if(InviteImpl().getInvites(user.id).find { it.albumId == album.id } == null){
                        call.respond(HttpStatusCode.Forbidden)
                        return@post
                    }

                    InviteImpl().acceptInvite(user.id, album.id)
                    call.respond(HttpStatusCode.OK)
                }
                post("decline"){
                    val user = NetUtil.getAuthUser(call) ?: return@post
                    val albumID = (NetUtil.getParamOrResponse(call, "albumID") ?: return@post).toInt()

                    if(InviteImpl().getInvites(user.id).find { it.albumId == albumID } == null){
                        call.respond(HttpStatusCode.Forbidden)
                        return@post
                    }

                    InviteImpl().declineInvite(user.id, albumID)
                    call.respond(HttpStatusCode.OK)
                }
                get("rights"){
                    val user = NetUtil.getAuthUser(call) ?: return@get
                    val albumID = (NetUtil.getParamOrResponse(call, "albumID") ?: return@get).toInt()

                    val album = AlbumImpl().getAlbum(albumID)

                    if(album == null){
                        call.respond(HttpStatusCode.BadRequest)
                        return@get
                    }

                    val res = AlbumUtil.getRights(user,album)
                    call.respond(res)
                }
            }
        }
    }
}