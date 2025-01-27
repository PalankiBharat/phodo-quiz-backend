package com.iapprusher.repo.question

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.utils.StringConstants.ID
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.iapprusher.application.data.response.PaginationMetadata
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
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

    override suspend fun getQuestionsByTagPaginated(tag: String, page: Int, size: Int): Pair<List<Question>, PaginationMetadata> {
        val internalTagFilter = Filters.eq(Tag::tag.name, tag)
        val filter = Filters.elemMatch(Question::tags.name, internalTagFilter)
        
        val totalRecords = collection.countDocuments(filter)
        val totalPages = ((totalRecords + size - 1) / size).toInt()
        val skipCount = (page - 1) * size
        
        val questions = if (skipCount >= totalRecords) {
            emptyList()
        } else {
            collection.find(filter)
                .skip(skipCount)
                .limit(size)
                .toList()
        }

        val metadata = PaginationMetadata(
            totalRecords = totalRecords.toInt(),
            currentPage = page,
            totalPages = totalPages,
            nextPage = if (page < totalPages) page + 1 else null,
            prevPage = if (page > 1) page - 1 else null
        )

        return Pair(questions, metadata)
    }


    override suspend fun getQuestionsPaginated(page: Int, size: Int): Pair<List<Question>, PaginationMetadata> {
        val totalRecords = getTotalQuestions()
        val totalPages = ((totalRecords + size - 1) / size).toInt()
        val skipCount = (page - 1) * size
        
        val questions = if (skipCount >= totalRecords) {
            emptyList()
        } else {
            collection.find()
                .skip(skipCount)
                .limit(size)
                .toList()
        }

        val metadata = PaginationMetadata(
            totalRecords = totalRecords.toInt(),
            currentPage = page,
            totalPages = totalPages,
            nextPage = if (page < totalPages) page + 1 else null,
            prevPage = if (page > 1) page - 1 else null
        )

        return Pair(questions, metadata)
    }

    override suspend fun getTotalQuestions(): Long {
        return collection.countDocuments()
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

    override suspend fun getRandomQuestionsByTag(tag: String, size: Int): List<Question> {
        val internalTagFilter = Filters.eq(Tag::tag.name, tag)
        val filter = Filters.elemMatch(Question::tags.name, internalTagFilter)
        
        return collection.aggregate(
            listOf(
                Aggregates.match(filter),
                Aggregates.sample(size)
            )
        ).toList()
    }
}