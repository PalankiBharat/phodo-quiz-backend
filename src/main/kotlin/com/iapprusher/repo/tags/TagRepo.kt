package com.iapprusher.repo.tags

import com.iapprusher.application.data.entity.Tag

interface TagRepo {
    suspend fun getAllTags(): List<Tag>
    suspend fun addTag(tag: String): Boolean
    suspend fun deleteTag(id: String): Boolean
    suspend fun getTagById(id: String): Tag?
    suspend fun updateTag(id: String, newTag: Tag): Boolean
    suspend fun areTagsPresent(tags: List<String>): Boolean
}