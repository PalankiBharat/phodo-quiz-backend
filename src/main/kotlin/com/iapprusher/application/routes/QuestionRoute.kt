package com.iapprusher.application.routes

import com.iapprusher.application.data.request.QuestionRequest
import com.iapprusher.application.utils.Routes
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
            post {
                val questionsParam = call.receive<QuestionRequest>()
                val response = questionsService.addQuestion(questionsParam)
                call.sendResponse(response)
            }
            delete {
                val questionId = call.parameters[Routes.Question.ID]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest)
                val deleteResponse = questionsService.deleteQuestion(questionId)
                call.sendResponse(deleteResponse)
            }
            post(path = "/{${Routes.Question.ID}}") {
                val editQuestionRequest = call.receive<QuestionRequest>()
                val questionId = call.parameters[Routes.Question.ID]
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                println("Question ID: $questionId")
                val editResponse = questionsService.updateQuestion(questionId, editQuestionRequest)
                call.sendResponse(editResponse)
            }
            get(path = "/{${Routes.Question.ID}}") {
                val questionId = call.parameters[Routes.Question.ID]
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val questionsResponse = questionsService.getQuestionById(questionId)
                call.sendResponse(questionsResponse)
            }

            get {
                val questionsResponse = questionsService.getAllQuestions()
                call.sendResponse(questionsResponse)
            }

            get("/tag/{${Routes.Tag.ROUTE}}") {
                val tag = call.parameters[Routes.Tag.ROUTE]
                    ?: return@get call.respond(HttpStatusCode.BadRequest)
                val questionsResponse = questionsService.getQuestionsByTag(tag)
                call.sendResponse(questionsResponse)
            }

        }
    }

}