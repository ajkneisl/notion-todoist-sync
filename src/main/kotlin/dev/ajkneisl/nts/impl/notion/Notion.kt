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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Notion {
    val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    private val CLIENT =
        HttpClient(CIO) {
            defaultRequest {
                header("Authorization", "Bearer ${NotionTodoistSync.NOTION_API_KEY}")
                header("Notion-Version", "2021-08-16")
            }

            install(JsonFeature) { serializer = KotlinxSerializer() }
        }

    /** Append Todoist Task to Notion Block */
    suspend fun appendBlock(block: JsonElement, blockId: String = NotionTodoistSync.BLOCK_ID) {
        if (blockId == "") {
            throw Exception("Block ID must be filled out!")
        }

        LOGGER.debug("Appending block to {}", blockId)

        try {
            CLIENT.patch<String>(
                "https://api.notion.com/v1/blocks/${blockId}/children"
            ) {
                contentType(ContentType.Application.Json)
                body =
                    buildJsonObject {
                        putJsonArray("children") {
                            add(block)
                        }
                    }
            }
        } catch (ex: ClientRequestException) {
            LOGGER.error("Block {} not found.", blockId)
        }
    }
}
