package com.iapprusher.repo.question

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.utils.StringConstants.ID
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.conversions.Bson
import org.bson.types.ObjectId

class QuestionRepoImpl(
    mongoCollection: MongoCollection<Question>,
) : QuestionRepo {
    private val collection = mongoCollection.withDocumentClass(Question::class.java)
    override suspend fun getAllQuestions(): List<Question> {
        return collection
            .find()
            .toList()
            .shuffled()
    }

    override suspend fun getQuestionsByTag(tag: String, size: Int?): List<Question> {
        val internalTagFilter = Filters.eq(Tag::tag.name, tag)
        val filter = Filters.elemMatch(Question::tags.name, internalTagFilter)
        val aggregates = mutableListOf(
            Aggregates.match(filter)
        )
        if (size != null) {
            aggregates.add(Aggregates.sample(size))
        }
        return collection.aggregate(
            aggregates
        ).toList()
    }

    override suspend fun getQuestionsPaginated(page: Int, size: Int): List<Question> {
        return collection.find()
            .skip((page - 1) * size)
            .limit(size)
            .toList()
    }

    override suspend fun getQuestionById(id: String): Question? {
        return collection.find(Filters.eq(ID, id)).firstOrNull()
    }

    override suspend fun addQuestion(question: Question): Boolean {
        return collection.insertOne(question).wasAcknowledged()
    }

    override suspend fun addAllQuestions(questions: List<Question>): Boolean {
        return collection.insertMany(questions).wasAcknowledged()
    }

    override suspend fun updateQuestion(id: String, question: Question): Question? {
        return collection.findOneAndReplace(Filters.eq(ID, ObjectId(id)), question)
    }

    override suspend fun deleteQuestion(id: String): Boolean {
        return collection.deleteOne(Filters.eq(ID, id)).wasAcknowledged()
    }
}