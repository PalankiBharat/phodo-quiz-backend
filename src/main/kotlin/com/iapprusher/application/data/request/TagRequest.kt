package com.iapprusher.application.data.request

import com.iapprusher.application.data.entity.Tag

data class TagRequest(
    val tag: String
){
    fun toTag(): Tag {
        return Tag(
            id = null,
            tag = tag
        )
    }
}