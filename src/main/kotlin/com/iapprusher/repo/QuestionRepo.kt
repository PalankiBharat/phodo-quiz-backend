package com.iapprusher.repo

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag

interface QuestionRepo {
    suspend fun getAllQuestions(): List<Question>
    suspend fun getQuestionById(id: String): Question?
    suspend fun addQuestion(question: Question): Boolean
    suspend fun addAllQuestions(questions: List<Question>): Boolean
    suspend fun updateQuestion(id: String, question: Question): Question?
    suspend fun deleteQuestion(id: String): Boolean
    suspend fun getQuestionsByTag(tag: Tag): List<Question>
    suspend fun getQuestionsPaginated(page: Int, size: Int): List<Question>
}