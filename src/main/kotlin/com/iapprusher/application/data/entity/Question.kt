package com.iapprusher.application.data.entity

import com.iapprusher.application.data.response.QuestionResponse
import com.iapprusher.application.utils.InstantAsBsonDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Question(
    @SerialName("_id")
    @BsonId val id: ObjectId?,
    val question: String,
    val tags: List<Tag>,
    val options: List<Option>,
    @Serializable(with = InstantAsBsonDateTime::class)
    val createdAt: Instant,
){
    fun toQuestionResponse(): QuestionResponse {
        return QuestionResponse(
            id = id.toString(),
            question = question,
            tags = tags.map { it.tag },
            options = options.map { it.option },
            createdAt = createdAt.toString()
        )
    }
}


@Serializable
data class Option(
    val option: String,
    val isCorrect: Boolean
)

data class Tag(
    @SerialName("_id")
    @BsonId val id: ObjectId?,
    val tag: String
)
