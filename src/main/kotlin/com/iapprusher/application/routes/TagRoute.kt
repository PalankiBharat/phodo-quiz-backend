package com.iapprusher.application.routes

import com.iapprusher.application.data.request.EditTagRequest
import com.iapprusher.application.data.request.TagRequest
import com.iapprusher.application.utils.Routes
import com.iapprusher.application.utils.sendResponse
import com.iapprusher.service.TagService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.tagRoute() {
    val tagService: TagService by application.inject()
    route(Routes.API_VERSION) {
        route(Routes.Tag.ROUTE) {
            post {
                val tagRequest = call.receive<TagRequest>()
                if (tagRequest.tag.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Tag cannot be blank")
                    return@post
                }
                val response = tagService.addTag(tagRequest.tag)
                call.sendResponse(response)
            }
            delete {
                val tagId = call.parameters[Routes.Tag.ID]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                if (tagId.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Tag ID cannot be blank")
                    return@delete
                }
                val deleteResponse = tagService.deleteTag(tagId)
                call.sendResponse(deleteResponse)
            }
            post(path = "/edit") {
                val editTagRequest = call.receive<EditTagRequest>()
                if (editTagRequest.id.isBlank() || editTagRequest.newTag.isBlank()) {
                    call.respond(HttpStatusCode.BadRequest, "Tags cannot be blank")
                    return@post
                }
                val editResponse = tagService.editTag(editTagRequest)
                call.sendResponse(editResponse)
            }
            get {
                val tagsResponse = tagService.getTags()
                call.sendResponse(tagsResponse)
            }
        }
    }
}