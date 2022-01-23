package dev.ajkneisl.nts.impl.todoist

import dev.ajkneisl.nts.NotionTodoistSync
import dev.ajkneisl.nts.impl.todoist.obj.Project
import dev.ajkneisl.nts.impl.todoist.obj.Task
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import java.util.*

object Todoist {
    private val CLIENT =
        HttpClient(CIO) {
            defaultRequest {
                header("Authorization", "Bearer ${NotionTodoistSync.TODOIST_API_KEY}")
            }

            install(JsonFeature) { serializer = KotlinxSerializer() }
        }

    /** Get user's projects */
    suspend fun getProjects(): List<Project> {
        return CLIENT.get("https://api.todoist.com/rest/v1/projects")
    }

    /** Get user's tasks */
    suspend fun getTasks(): List<Task> {
        return CLIENT.get("https://api.todoist.com/rest/v1/tasks")
    }
}
