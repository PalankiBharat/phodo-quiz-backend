package com.iapprusher.application.data.request

import com.iapprusher.application.data.entity.Tag

data class EditTagRequest(
    val id:String,
    val newTag:String
){
    fun toTag(): Tag {
        return Tag(
            id = null,
            tag = newTag
        )
    }
}
