package com.iapprusher.application.data.entity

import kotlinx.serialization.SerialName
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Tag(
    @SerialName("_id")
    @BsonId val id: ObjectId? = null,
    val tag: String
)
