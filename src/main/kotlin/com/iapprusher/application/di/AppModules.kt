package com.iapprusher.application.di

import com.iapprusher.application.data.entity.Question
import com.iapprusher.application.data.entity.Tag
import com.iapprusher.application.plugins.connectToMongoDB
import com.iapprusher.application.utils.StringConstants.QUESTIONS_COLLECTION_NAME
import com.iapprusher.application.utils.StringConstants.TAG_COLLECTION_NAME
import com.iapprusher.repo.QuestionRepo
import com.iapprusher.repo.QuestionRepoImpl
import com.iapprusher.repo.TagRepo
import com.iapprusher.repo.TagRepoImpl
import com.iapprusher.service.QuestionService
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
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
                single { QuestionRepoImpl(get((named("question")))) } bind QuestionRepo::class
                single { QuestionService(get()) }
                single { TagRepoImpl(get(named("tag"))) } bind TagRepo::class

            }
        )
    }
}

private val repositoryModule = module {
    single { QuestionRepoImpl(get()) } bind QuestionRepo::class
}

private val collectionModule = module {
    single { provideQuestionCollection(get()) }
}

private val serviceModule = module {
    single { QuestionService(get()) }

}

fun provideQuestionCollection(database: MongoDatabase): MongoCollection<Question> {
    return database.getCollection(QUESTIONS_COLLECTION_NAME, Question::class.java)
}

fun provideTagsCollection(database: MongoDatabase): MongoCollection<Tag> {
    return database.getCollection(TAG_COLLECTION_NAME, Tag::class.java)
}
