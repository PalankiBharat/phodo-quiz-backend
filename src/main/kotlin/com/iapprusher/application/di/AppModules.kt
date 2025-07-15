package com.iapprusher.application.di

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.data.entity.User
import com.iapprusher.application.plugins.connectToMongoDB
import com.iapprusher.application.utils.StringConstants.QUESTIONS_COLLECTION_NAME
import com.iapprusher.application.utils.StringConstants.TAG_COLLECTION_NAME
import com.iapprusher.application.utils.StringConstants.USER_COLLECTION_NAME
import com.iapprusher.repo.question.QuestionRepo
import com.iapprusher.repo.question.QuestionRepoImpl
import com.iapprusher.repo.tags.TagRepo
import com.iapprusher.repo.tags.TagRepoImpl
import com.iapprusher.repo.user.UserRepo
import com.iapprusher.repo.user.UserRepoImpl
import com.iapprusher.service.AuthService
import com.iapprusher.service.QuestionService
import com.iapprusher.service.TagService
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI() {
    install(Koin) {
        slf4jLogger()
        modules(
            module {
                single { connectToMongoDB() }
                single(named("question")) { provideQuestionCollection(get()) }
                single(named("tag")) { provideTagsCollection(get()) }
                single(named("user")) { provideUserCollection(get()) }
                single { QuestionRepoImpl(get((named("question")))) } bind QuestionRepo::class
                single { QuestionService(get(), get()) }
                single { TagRepoImpl(get(named("tag"))) } bind TagRepo::class
                single { TagService(get()) }
                single { UserRepoImpl(get(named("user"))) } bind UserRepo::class
                single { AuthService(get(),this@configureDI) }
            }
        )
    }
}

fun provideQuestionCollection(database: MongoDatabase): MongoCollection<Question> {
    return database.getCollection(QUESTIONS_COLLECTION_NAME, Question::class.java)
}

fun provideTagsCollection(database: MongoDatabase): MongoCollection<Tag> {
    return database.getCollection(TAG_COLLECTION_NAME, Tag::class.java)
}

fun provideUserCollection(database: MongoDatabase): MongoCollection<User> {
    return database.getCollection(USER_COLLECTION_NAME, User::class.java)
}
