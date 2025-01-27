package com.iapprusher.application.routes

import com.iapprusher.application.data.request.QuestionRequest
import com.iapprusher.application.utils.Routes
import com.iapprusher.application.utils.Routes.PAGE
import com.iapprusher.application.utils.Routes.SIZE
import com.iapprusher.application.utils.sendResponse
import com.iapprusher.service.QuestionService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.questionRoute() {
    val questionsService: QuestionService by application.inject()
    route(Routes.API_VERSION) {
        route(Routes.Question.ROUTE) {
            addQuestion(questionsService)
            deleteQuestion(questionsService)
            updateQuestion(questionsService)
            getQuestionById(questionsService)
            getPaginatedQuestions(questionsService)
            getQuestionsByTagPaginated(questionsService)
            getRandomQuestionsByTag(questionsService)
        }
    }
}

/**
 * POST /api/v1/questions
 * Creates a new question in the system.
 *
 * @request body QuestionRequest - The question details including title, content, and tags
 */
private fun Route.addQuestion(questionsService: QuestionService) {
    post {
        val questionsParam = call.receive<QuestionRequest>()
        val response = questionsService.addQuestion(questionsParam)
        call.sendResponse(response)
    }
}

/**
 * DELETE /api/v1/questions/{id}
 * Deletes a specific question by its ID.
 *
 * @param id The unique identifier of the question to delete
 */
private fun Route.deleteQuestion(questionsService: QuestionService) {
    delete {
        val questionId = call.parameters[Routes.Question.ID]
            ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val deleteResponse = questionsService.deleteQuestion(questionId)
        call.sendResponse(deleteResponse)
    }
}

/**
 * POST /api/v1/questions/{id}
 * Updates an existing question by its ID.
 *
 * @param id The unique identifier of the question to update
 * @request body QuestionRequest - The updated question details
 */
private fun Route.updateQuestion(questionsService: QuestionService) {
    post(path = "/{${Routes.Question.ID}}") {
        val editQuestionRequest = call.receive<QuestionRequest>()
        val questionId = call.parameters[Routes.Question.ID]
            ?: return@post call.respond(HttpStatusCode.BadRequest)
        println("Question ID: $questionId")
        val editResponse = questionsService.updateQuestion(questionId, editQuestionRequest)
        call.sendResponse(editResponse)
    }
}

/**
 * GET /api/v1/questions/{id}
 * Retrieves a specific question by its ID.
 *
 * @param id The unique identifier of the question to retrieve
 */
private fun Route.getQuestionById(questionsService: QuestionService) {
    get(path = "/{${Routes.Question.ID}}") {
        val questionId = call.parameters[Routes.Question.ID]
            ?: return@get call.respond(HttpStatusCode.BadRequest)
        val questionsResponse = questionsService.getQuestionById(questionId)
        call.sendResponse(questionsResponse)
    }
}

/**
 * GET /api/v1/questions?page={page}&size={size}
 * Retrieves a paginated list of all questions.
 *
 * @param page Optional: The page number (defaults to 1)
 * @param size Optional: Number of items per page (defaults to 10)
 */
private fun Route.getPaginatedQuestions(questionsService: QuestionService) {
    get {
        val page = call.parameters[PAGE]?.toIntOrNull() ?: 1
        val size = call.parameters[SIZE]?.toIntOrNull() ?: 10
        val questionsResponse = questionsService.getQuestionsPaginated(page, size)
        call.sendResponse(questionsResponse)
    }
}

/**
 * GET /api/v1/questions/tags?tag={tag}&page={page}&size={size}
 * Retrieves a paginated list of questions filtered by a specific tag.
 *
 * @param tag Required: The tag to filter questions by
 * @param page Optional: The page number (defaults to 1)
 * @param size Optional: Number of items per page (defaults to 10)
 */
private fun Route.getQuestionsByTagPaginated(questionsService: QuestionService) {
    get("/${Routes.Tag.ROUTE}") {
        val tag = call.parameters[Routes.Tag.ROUTE]
            ?: return@get call.respond(HttpStatusCode.BadRequest)
        val page = call.parameters[PAGE]?.toIntOrNull() ?: 1
        val size = call.parameters[SIZE]?.toIntOrNull() ?: 10
        val questionsResponse = questionsService.getQuestionsByTagPaginated(tag, page, size)
        call.sendResponse(questionsResponse)
    }
}

/**
 * GET /api/v1/questions/tags/random?tag={tag}&size={size}
 * Retrieves a random set of questions for a specific tag.
 *
 * @param tag Required: The tag to filter questions by
 * @param size Optional: Number of random questions to return (defaults to 10)
 */
private fun Route.getRandomQuestionsByTag(questionsService: QuestionService) {
    get("/${Routes.Tag.ROUTE}/random") {
        val tag = call.parameters[Routes.Tag.ROUTE]
            ?: return@get call.respond(HttpStatusCode.BadRequest)
        val size = call.parameters[SIZE]?.toIntOrNull() ?: 10
        val questionsResponse = questionsService.getRandomQuestionsByTag(tag, size)
        call.sendResponse(questionsResponse)
    }
}