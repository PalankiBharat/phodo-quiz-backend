package com.iapprusher.application.data.response

import com.iapprusher.application.data.entity.Option
import com.iapprusher.application.data.entity.Tag

data class QuestionResponse(
    val id: String,
    val question: String,
    val tags: List<Tag>,
    val options: List<Option>,
    val createdAt: String,
    val questionImage:String
)



data class PaginatedQuestionResponse(
    val questions: List<QuestionResponse>,
    val pagination: PaginationMetadata
)

data class PaginationMetadata(
    val totalRecords: Int,
    val currentPage: Int,
    val totalPages: Int,
    val nextPage: Int?,
    val prevPage: Int?
)