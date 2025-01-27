package com.iapprusher.service

import com.iapprusher.application.utils.okResult
import com.iapprusher.application.data.request.QuestionRequest
import com.iapprusher.application.data.response.BasicResponseModel
import com.iapprusher.application.data.response.QuestionResponse
import com.iapprusher.application.data.response.PaginatedQuestionResponse
import com.iapprusher.application.data.response.failureResponse
import com.iapprusher.application.data.response.successResponse
import com.iapprusher.application.utils.safeServerCall
import com.iapprusher.repo.question.QuestionRepo
import com.iapprusher.repo.tags.TagRepo
import io.ktor.http.*

class QuestionService(
    private val repo: QuestionRepo,
    private val tagRepo: TagRepo
) {
    suspend fun getAllQuestions(): Pair<HttpStatusCode, BasicResponseModel<List<QuestionResponse>>> {
        return safeServerCall {
            val questions = repo.getAllQuestions().map {
                it.toQuestionResponse()
            }
            if (questions.isEmpty()) {
                okResult(failureResponse("No questions found"))
            } else {
                okResult(successResponse("Questions found", questions))
            }
        }
    }

    suspend fun addQuestion(question: QuestionRequest): Pair<HttpStatusCode, BasicResponseModel<Boolean>> {
        return safeServerCall {
            val areTagsPresent = tagRepo.areTagsPresent(question.tags.map { it.tag })
            if (!areTagsPresent) {
                return@safeServerCall okResult(failureResponse("All Tags Do Not Exist"))
            }
            val questionEntity = question.toQuestion()
            val result = repo.addQuestion(questionEntity)
            if (result) {
                okResult(successResponse("Question added", true))
            } else {
                okResult(failureResponse("Question not added"))
            }
        }
    }

    suspend fun getQuestionById(id: String): Pair<HttpStatusCode, BasicResponseModel<QuestionResponse>> {
        return safeServerCall {
            val question = repo.getQuestionById(id)
            if (question != null) {
                okResult(successResponse("Question found", question.toQuestionResponse()))
            } else {
                okResult(failureResponse("Question not found"))
            }
        }
    }

    suspend fun updateQuestion(
        id: String,
        question: QuestionRequest
    ): Pair<HttpStatusCode, BasicResponseModel<QuestionResponse>> {
        return safeServerCall {
            val areTagsPresent = tagRepo.areTagsPresent(question.tags.map { it.tag })
            if (!areTagsPresent) {
                return@safeServerCall okResult(failureResponse("All Tags Do Not Exist"))
            }
            val questionEntity = question.toQuestion()
            val updatedQuestion = repo.updateQuestion(id, questionEntity)
            if (updatedQuestion != null) {
                okResult(successResponse("Question updated", updatedQuestion.toQuestionResponse()))
            } else {
                okResult(failureResponse("Question not updated"))
            }
        }
    }

    suspend fun deleteQuestion(id: String): Pair<HttpStatusCode, BasicResponseModel<Boolean>> {
        return safeServerCall {
            val result = repo.deleteQuestion(id)
            if (result) {
                okResult(successResponse("Question deleted", true))
            } else {
                okResult(failureResponse("Question not deleted"))
            }
        }
    }

    suspend fun getQuestionsByTagPaginated(
        tag: String,
        page: Int,
        size: Int
    ): Pair<HttpStatusCode, BasicResponseModel<PaginatedQuestionResponse>> {
        return safeServerCall {
            val (questions, paginationMetadata) = repo.getQuestionsByTagPaginated(tag, page, size)
            val paginatedResponse = PaginatedQuestionResponse(
                questions = questions.map { it.toQuestionResponse() },
                pagination = paginationMetadata
            )
            if (questions.isEmpty()) {
                okResult(failureResponse("No questions found"))
            } else {
                okResult(successResponse("Questions found", paginatedResponse))
            }
        }
    }

    suspend fun getQuestionsPaginated(
        page: Int,
        size: Int
    ): Pair<HttpStatusCode, BasicResponseModel<PaginatedQuestionResponse>> {
        return safeServerCall {
            val (questions, paginationMetadata) = repo.getQuestionsPaginated(page, size)
            val paginatedResponse = PaginatedQuestionResponse(
                questions = questions.map { it.toQuestionResponse() },
                pagination = paginationMetadata
            )
            okResult(successResponse("Questions found", paginatedResponse))
        }
    }

    suspend fun getRandomQuestionsByTag(
        tag: String,
        size: Int
    ): Pair<HttpStatusCode, BasicResponseModel<List<QuestionResponse>>> {
        return safeServerCall {
            val questions = repo.getRandomQuestionsByTag(tag, size)
            if (questions.isEmpty()) {
                okResult(failureResponse("No questions found"))
            } else {
                okResult(successResponse("Questions found", questions.map { it.toQuestionResponse() }))
            }
        }
    }
}
