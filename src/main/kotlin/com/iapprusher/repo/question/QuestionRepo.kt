package com.iapprusher.repo.question

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.response.PaginationMetadata


interface QuestionRepo {
    suspend fun getAllQuestions(): List<Question>
    suspend fun getQuestionById(id: String): Question?
    suspend fun addQuestion(question: Question): Boolean
    suspend fun addAllQuestions(questions: List<Question>): Boolean
    suspend fun updateQuestion(id: String, question: Question): Question?
    suspend fun deleteQuestion(id: String): Boolean
    suspend fun getQuestionsByTagPaginated(tag: String, page: Int, size: Int): Pair<List<Question>, PaginationMetadata>
    suspend fun getQuestionsPaginated(page: Int, size: Int): Pair<List<Question>, PaginationMetadata>
    suspend fun getTotalQuestions(): Long
    suspend fun getRandomQuestionsByTag(tag: String, size: Int): List<Question>
}