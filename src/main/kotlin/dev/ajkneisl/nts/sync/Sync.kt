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
            for (task in tasks) {
                if (!CURRENT_TASK_IDS.contains(task.id)) {
                    LOGGER.info("Found new task! {}", task.id)
                    Notion.appendBlock(task.toBlock())
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