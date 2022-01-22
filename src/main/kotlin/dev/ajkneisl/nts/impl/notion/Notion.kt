package dev.ajkneisl.nts.impl.notion

import dev.ajkneisl.nts.NotionTodoistSync
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.lang.Exception
import kotlinx.serialization.json.*

object Notion {
    private val CLIENT =
        HttpClient(CIO) {
            defaultRequest {
                header("Authorization", "Bearer ${NotionTodoistSync.NOTION_API_KEY}")
                header("Notion-Version", "2021-08-16")
            }

            install(JsonFeature) { serializer = KotlinxSerializer() }
        }

    /** Append Todoist Task to Notion Block */
    suspend fun appendBlock(block: JsonElement) {
        if (NotionTodoistSync.BLOCK_ID == "") {
            throw Exception("Block ID must be filled out!")
        }

        println(
            CLIENT.patch<String>(
                "https://api.notion.com/v1/blocks/${NotionTodoistSync.BLOCK_ID}/children"
            ) {
                contentType(ContentType.Application.Json)
                body =
                    buildJsonObject {
                        putJsonArray("children") {
                            add(block)
                        }
                    }
            }
        )
    }
}
