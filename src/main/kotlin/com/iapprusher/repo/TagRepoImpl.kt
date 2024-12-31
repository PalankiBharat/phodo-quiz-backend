package com.iapprusher.repo

import com.iapprusher.application.data.entity.Tag
import com.mongodb.client.model.Filters
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class TagRepoImpl(
    private val mongoCollection: MongoCollection<Tag>
) : TagRepo {
    private val collection = mongoCollection.withDocumentClass(Tag::class.java)
    override suspend fun getAllTags(): List<Tag> {
        return collection
            .find()
            .toList()
    }

    override suspend fun addTag(tag: String): Boolean {
        collection.createIndex(Indexes.text(Tag::tag.name), IndexOptions().unique(true))
        return collection.insertOne(Tag(tag = tag)).wasAcknowledged()
    }

    override suspend fun deleteTag(id: String): Boolean {
        return collection.deleteOne(Filters.eq(Tag::id.name, id)).wasAcknowledged()
    }

    override suspend fun getTagById(id: String): Tag? {
        return collection.find(Filters.eq(Tag::id.name, id)).firstOrNull()
    }

    override suspend fun updateTag(id: String, newTag: Tag): Tag? {
        return collection.findOneAndReplace(Filters.eq(Tag::id.name, id), newTag)
    }

}