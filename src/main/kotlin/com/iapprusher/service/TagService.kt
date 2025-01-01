package com.iapprusher.service

import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.data.request.EditTagRequest
import com.iapprusher.application.data.response.BasicResponseModel
import com.iapprusher.application.data.response.failureResponse
import com.iapprusher.application.data.response.successResponse
import com.iapprusher.application.utils.okResult
import com.iapprusher.application.utils.safeServerCall
import com.iapprusher.repo.tags.TagRepo
import io.ktor.http.*

class TagService(
    private val tagRepository: TagRepo
) {
    suspend fun getTags(): Pair<HttpStatusCode, BasicResponseModel<List<Tag>>> {
        return safeServerCall {
            val tags = tagRepository.getAllTags()
            if (tags.isEmpty()) {
                okResult(failureResponse("No tags found"))
            } else {
                okResult(successResponse("Tags found", tags))
            }
        }
    }

    suspend fun addTag(tag: String): Pair<HttpStatusCode, BasicResponseModel<Boolean>> {
        return safeServerCall {
            val result = tagRepository.addTag(tag)
            if (result) {
                okResult(successResponse("Tag added", true))
            } else {
                okResult(failureResponse("Tag not added"))
            }
        }
    }

    suspend fun deleteTag(id: String): Pair<HttpStatusCode, BasicResponseModel<Boolean>> {
        return safeServerCall {
            val result = tagRepository.deleteTag(id)
            if (result) {
                okResult(successResponse("Tag deleted", true))
            } else {
                okResult(failureResponse("Tag not deleted"))
            }
        }
    }

    suspend fun editTag(editTagRequest: EditTagRequest): Pair<HttpStatusCode, BasicResponseModel<Tag>> {
        return safeServerCall {
            val result = tagRepository.updateTag(editTagRequest.id, editTagRequest.toTag())
            if (result != null) {
                okResult(successResponse("Tag edited", result))
            } else {
                okResult(failureResponse("Tag not edited"))
            }
        }
    }
}