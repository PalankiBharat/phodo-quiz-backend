package com.iapprusher.application.data.response

data class QuestionResponse(
    val id: String,
    val question: String,
    val tags: List<String>,
    val options: List<String>,
    val createdAt: String
)
