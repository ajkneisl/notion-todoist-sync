package dev.ajkneisl.nts.impl.todoist.obj

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Long,
    val name: String,
    val color: Int,
    @SerialName("parent_id") val parentId: Int? = null,
    val order: Int? = null,
    @SerialName("comment_count") val commentCount: Int,
    val shared: Boolean,
    val favorite: Boolean,
    @SerialName("inbox_project") val inboxProject: Boolean? = null,
    @SerialName("team_inbox") val teamInbox: Boolean? = null,
    @SerialName("sync_id") val syncId: Int,
    val url: String
)
