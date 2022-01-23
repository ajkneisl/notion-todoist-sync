package dev.ajkneisl.nts.impl.todoist.obj

import dev.ajkneisl.nts.impl.notion.Notion
import dev.ajkneisl.nts.impl.notion.Widget
import dev.ajkneisl.nts.impl.todoist.Todoist
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.*

@Serializable
data class Task(
    val id: Long,
    @SerialName("project_id") val projectId: Long,
    @SerialName("section_id") val sectionId: Long,
    val content: String,
    val description: String,
    val completed: Boolean,
    @SerialName("label_ids") val labelIds: List<Long>,
    @SerialName("parent_id") val parentId: Int? = null,
    val order: Int,
    val priority: Int,
    val due: Due? = null,
    val url: String,
    @SerialName("comment_count") val commentCount: Int,
    val assignee: Int? = null,
    val assigner: Int,
    val creator: Int,
    val created: String? = null
)

fun Task.toBlock(): JsonElement {
    return buildJsonObject {
        put("object", "block")
        put("type", "embed")
        putJsonObject("embed") {
            put("url", Widget.getWidgetUrl(this@toBlock.id))
        }
    }
}

@Serializable
data class Due(
    val string: String,
    val date: String,
    val recurring: Boolean,
    @SerialName("datetime") val dateTime: String? = null,
    val timezone: String? = null,
    val lang: String? = null
)
