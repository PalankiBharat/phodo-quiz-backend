package com.iapprusher.application.data.request

import com.iapprusher.application.data.entity.Option
import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag
import kotlinx.datetime.Clock

data class QuestionRequest(
    val question: String,
    val tags: List<TagRequest>,
    val options: List<Option>
){
    fun toQuestion(): Question {
        return Question(
            id = null,
            question = question,
            tags = tags.map { it.toTag() },
            options = options,
            createdAt = Clock.System.now()
        )
    }
}