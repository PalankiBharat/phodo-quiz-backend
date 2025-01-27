package com.iapprusher.application.data.copyclasses

import com.iapprusher.application.data.entity.Option
import com.iapprusher.application.data.request.QuestionRequest
import com.iapprusher.application.data.request.TagRequest
import com.iapprusher.repo.tags.TagRepo
import com.iapprusher.service.QuestionService
import com.iapprusher.service.TagService
import io.ktor.server.application.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject
import java.io.File

@Serializable
data class QuestionCopy(
    val id: String,
    val qnumber: String,
    val sectionid: String,
    val catid: String,
    val sub_cat_id: String,
    val question: String,
    val question_image: String,
    val option1: String?,
    val option1_image: String?,
    val option2: String?,
    val option2_image: String?,
    val option3: String?,
    val option3_image: String?,
    val option4: String?,
    val option4_image: String?,
    val option5: String?,
    val option5_image: String?,
    val correct_option: String,
    val introtext: String,
    val fulltext: String,
    val explanation_image: String,
    val difficulty_level: String,
    val comments_count: String,
    val sub_cat_name: String?,
    val video_keyword: String?
)

const val ImageSuffix = ".png"
fun mapToMongoQuestion(jsonQuestion: QuestionCopy, tag: String): QuestionRequest {
    val options = arrayListOf<Option>()
    val tags = arrayListOf<TagRequest>()

    val ImageBaseUrl = "https://jeefocus.com/assets/images/question/${jsonQuestion.sectionid}/"

    if (jsonQuestion.option1 != null || jsonQuestion.option1_image != null) {
        val option1Image =
            if (!jsonQuestion.option1_image.isNullOrBlank()) ImageBaseUrl + jsonQuestion.option1_image + ImageSuffix else ""
        val areBothOptionsBlank = option1Image.isBlank() && jsonQuestion.option1?.isBlank() == true
        if (!areBothOptionsBlank) {
            options.add(
                Option(
                    option = jsonQuestion.option1 ?: "",
                    isCorrect = jsonQuestion.correct_option == "1",
                    optionImage = option1Image
                )
            )
        }
    }

    if (jsonQuestion.option2 != null || jsonQuestion.option2_image != null) {
        val option2Image =
            if (!jsonQuestion.option2_image.isNullOrBlank()) ImageBaseUrl + jsonQuestion.option2_image + ImageSuffix else ""
        val areBothOptionsBlank = option2Image.isBlank() && jsonQuestion.option2?.isBlank() == true
        if (!areBothOptionsBlank) {
            options.add(
                Option(
                    option = jsonQuestion.option2 ?: "",
                    isCorrect = jsonQuestion.correct_option == "2",
                    optionImage = option2Image
                )
            )
        }
    }

    if (jsonQuestion.option3 != null || jsonQuestion.option3_image != null) {
        val option3Image =
            if (!jsonQuestion.option3_image.isNullOrBlank()) ImageBaseUrl + jsonQuestion.option3_image + ImageSuffix else ""
        val areBothOptionsBlank = option3Image.isBlank() && jsonQuestion.option3?.isBlank() == true
        if (!areBothOptionsBlank) {
            options.add(
                Option(
                    option = jsonQuestion.option3 ?: "",
                    isCorrect = jsonQuestion.correct_option == "3",
                    optionImage = option3Image
                )
            )
        }
    }

    if (jsonQuestion.option4 != null || jsonQuestion.option4_image != null) {
        val option4Image =
            if (!jsonQuestion.option4_image.isNullOrBlank()) ImageBaseUrl + jsonQuestion.option4_image + ImageSuffix else ""
        val areBothOptionsBlank = option4Image.isBlank() && jsonQuestion.option4?.isBlank() == true
        if (!areBothOptionsBlank) {
            options.add(
                Option(
                    option = jsonQuestion.option4 ?: "",
                    isCorrect = jsonQuestion.correct_option == "4",
                    optionImage = option4Image
                )
            )
        }
    }

    if (jsonQuestion.option5 != null || jsonQuestion.option5_image != null) {
        val option5Image =
            if (!jsonQuestion.option5_image.isNullOrBlank()) ImageBaseUrl + jsonQuestion.option5_image + ImageSuffix else ""
        val areBothOptionsBlank = option5Image.isBlank() && jsonQuestion.option5?.isBlank() == true
        if (!areBothOptionsBlank) {
            options.add(
                Option(
                    option = jsonQuestion.option5 ?: "",
                    isCorrect = jsonQuestion.correct_option == "5",
                    optionImage = option5Image
                )
            )
        }
    }

    jsonQuestion.sub_cat_name?.let {
        tags.add(TagRequest(it))
    }
    tags.add(TagRequest("Mathematics"))
    tags.add(TagRequest(tag))

    val questionImage =
        if (jsonQuestion.question_image.isNotBlank()) ImageBaseUrl + jsonQuestion.question_image + ImageSuffix else ""
    return QuestionRequest(
        question = jsonQuestion.question,
        tags = tags, // Add `sub_cat_name` as a tag
        options = options, // Exclude blank options
        questionImage = questionImage,
    )
}

val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
fun Application.readJsonFileExample() {

    val tagService by inject<TagService>()
    val tagRepo by inject<TagRepo>()
    val questionService by inject<QuestionService>()
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    val totalQuestionList = arrayListOf<QuestionRequest>()

    var failureCount = 0

    val failureTagsList = arrayListOf<String>()

    val tagList = arrayListOf<TagRequest>()

    scope.launch {
        readAllFilesAndDoSomething { jsonString, fileName ->
            val questionSheetData = json.decodeFromString<Data>(jsonString)
            val questionListList = questionSheetData.fields.map {
                mapToMongoQuestion(it, fileName)
            }
            totalQuestionList.addAll(questionListList)
            val tags = questionListList.flatMap { it.tags }
            tagList.addAll(tags)

        }

        uploadQuestionData(totalQuestionList, questionService)
      //  uploadTags(tagList.distinct(),tagService)
    }

}

fun uploadTags(tagList: List<TagRequest>, tagService: TagService) {
    tagList.chunked(10).forEach {
        it.forEach {
            scope.launch {
                val data = tagService.addTag(it.tag)
                println("Status = ${data.second.status}  Message = ${data.second.message}  Data = ${it.tag}  Code = ${data.first.description}")
            }
        }

    }
}

fun uploadQuestionData(questionList: List<QuestionRequest>, questionService: QuestionService) {
    questionList.chunked(30).forEach {
        scope.launch {
            it.forEach {
                val data = questionService.addQuestion(it)
                println("Status = ${data.second.status}  Message = ${data.second.message}  Data = ${data.second.data}  Code = ${data.first.description}  Question = ${it.question}")
            }
        }
    }
}

suspend fun Application.readAllFilesAndDoSomething(uploadData: (jsonString: String, fileName: String) -> Unit) {
    // Path to the JSON file in the resources folder
    val resourceFolder = this::class.java.classLoader.getResource("test_sheets")?.path
        ?: throw IllegalArgumentException("Folder test_sheets not found in resources.")
    val folder = File(resourceFolder)
    if (folder.exists() && folder.isDirectory) {
        folder.listFiles { file -> file.extension == "json" }?.forEach { jsonFile ->
            val jsonContent = withContext(Dispatchers.IO) {
                jsonFile.readText()
            }
            uploadData(jsonContent, jsonFile.name.replace(".json", ""))
        }
    } else {
        println("Folder test_sheets is not a directory or does not exist.")
    }
}

fun <T> findDifferentElements(list1: List<T>, list2: List<T>): List<T> {
    val difference = list1.subtract(list2) // Elements in list1 but not in list2
    return difference.toList()
}

@Serializable
data class Data(
    val fields: List<QuestionCopy>
)

fun saveValueToFile(fileName: String, value: String) {
    // Create or overwrite the file and write the value
    val file = File(fileName)
    file.writeText(value)
    println("Value saved to $fileName")
}