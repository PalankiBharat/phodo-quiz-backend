package com.iapprusher.application.data.response

import com.iapprusher.application.data.entity.Option
import com.iapprusher.application.data.entity.Tag

data class QuestionResponse(
    val id: String,
    val question: String,
    val tags: List<Tag>,
    val options: List<Option>,
    val createdAt: String
)
