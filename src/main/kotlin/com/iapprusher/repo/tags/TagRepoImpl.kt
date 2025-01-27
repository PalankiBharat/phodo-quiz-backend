package com.iapprusher.repo.tags

import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.utils.StringConstants.ID
import com.mongodb.client.model.*
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class TagRepoImpl(
    mongoCollection: MongoCollection<Tag>
) : TagRepo {
    private val collection = mongoCollection.withDocumentClass(Tag::class.java)
    override suspend fun getAllTags(): List<Tag> {
        return collection
            .find()
            .toList()
    }

    override suspend fun addTag(tag: String): Boolean {
        collection.createIndex(Indexes.ascending(Tag::tag.name), IndexOptions().unique(true))
        return collection.insertOne(Tag(tag = tag)).wasAcknowledged()
    }

    override suspend fun deleteTag(id: String): Boolean {
        return collection.deleteOne(Filters.eq(ID, ObjectId(id))).wasAcknowledged()
    }

    override suspend fun getTagById(id: String): Tag? {
        return collection.find(Filters.eq(ID, ObjectId(id))).firstOrNull()
    }

    override suspend fun updateTag(id: String, newTag: Tag): Boolean {
        val update = Updates.set(Tag::tag.name, newTag.tag)
        return collection.updateOne(Filters.eq(ID, ObjectId(id)), update).wasAcknowledged()
    }

    override suspend fun areTagsPresent(tags: List<String>): Boolean {
        val tagsCollected = collection.countDocuments(Filters.`in`(Tag::tag.name, tags))
        val check = tagsCollected == tags.distinct().size.toLong()
        return check
    }

}