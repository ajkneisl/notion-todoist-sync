package dev.ajkneisl.nts.impl.notion

import dev.ajkneisl.nts.NotionTodoistSync
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonObject
import java.util.*

/**
 * The Todoist task widget itself.
 */
object Widget {
    var BACKGROUND_COLOR = "#303437"

    fun handleColorArgument(str: String) {
        BACKGROUND_COLOR = when (str.lowercase()) {
            "light" -> "#FFFFFF"
            "dark" -> "#303437"
            else -> str
        }
    }

    /** Get a widget url from Task id */
    fun getWidgetUrl(id: Long): String {
        val encode = Base64
            .getEncoder()
            .encodeToString(buildJsonObject {
                put("backgroundColor", BACKGROUND_COLOR)
                putJsonObject("queryParameters") {
                    put("task", id)
                    put("auth", NotionTodoistSync.TODOIST_API_KEY)
                }
            }.toString().toByteArray())

        return "https://notion-widget-host.vercel.app/todoist?val=${encode}"
    }
}