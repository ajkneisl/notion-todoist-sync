package dev.ajkneisl.nts.sync

import dev.ajkneisl.nts.NotionTodoistSync
import dev.ajkneisl.nts.impl.notion.Notion
import dev.ajkneisl.nts.impl.todoist.Todoist
import dev.ajkneisl.nts.impl.todoist.obj.toBlock
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration

object Sync {
    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)
    var LAST_SYNC: Long = 0
    private var CURRENT_TASK_IDS = mutableListOf<Long>()

    suspend fun syncTasks(initialSync: Boolean = false) {
        LAST_SYNC = System.currentTimeMillis()
        LOGGER.debug("Beginning sync...")

        val tasks = Todoist.getTasks()

        if (!initialSync) {
            val projects = Todoist.getProjects()

            for (task in tasks) {
                if (!CURRENT_TASK_IDS.contains(task.id)) {
                    var blockId = NotionTodoistSync.BLOCK_ID
                    LOGGER.info("Found new task! {}", task.id)

                    if (NotionTodoistSync.BLOCK_USE_PROJECT_SPECIFIC) {
                        val project = projects.find { project -> project.id == task.projectId }

                        if (project != null) {
                            LOGGER.debug("Project Specific: Looking for project: {}, Block tied projects: {}", project.name, NotionTodoistSync.BLOCK_TIED_PROJECT)
                            if (NotionTodoistSync.BLOCK_TIED_PROJECT.containsKey(project.name.lowercase())) {
                                blockId = NotionTodoistSync.BLOCK_TIED_PROJECT[project.name.lowercase()] ?: blockId
                                LOGGER.info("Task {} using project block ID: {}", task.id, blockId)
                            }
                        }
                    }

                    Notion.appendBlock(task.toBlock(), blockId = blockId)
                }
            }
        }

        CURRENT_TASK_IDS = tasks.map { task -> task.id }.toMutableList()

        LOGGER.debug("Sync complete. Scheduled for {}ms in the future.", NotionTodoistSync.REFRESH_INTERVAL)

        GlobalScope.launch {
            delay(NotionTodoistSync.REFRESH_INTERVAL)
            syncTasks()
        }
    }
}