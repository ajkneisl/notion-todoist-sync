package dev.ajkneisl.nts

import dev.ajkneisl.nts.impl.notion.Notion
import dev.ajkneisl.nts.impl.todoist.Todoist
import dev.ajkneisl.nts.sync.Sync
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object NotionTodoistSync {
    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)
    var NOTION_API_KEY = ""
    var TODOIST_API_KEY = ""
    var BLOCK_ID = ""
    var DATABASE_ID = ""
    var REFRESH_INTERVAL: Long = 1000 * 60 * 5

    suspend fun main(args: Array<String>) {
        parseArgs(
            args,
            hashMapOf(
                "--use_block" to { BLOCK_ID = it },
                "--use_database" to { DATABASE_ID = it },
                "--notion" to { NOTION_API_KEY = it },
                "--todoist" to { TODOIST_API_KEY = it },
                "--refresh" to { REFRESH_INTERVAL = it.toLong() }
            )
        )

        Sync.syncTasks(false)
        while (true) {
            delay(100)
        }
    }

    /** Parse [args] through [handle] to fill various variables. */
    private fun parseArgs(args: Array<String>, handle: HashMap<String, (String) -> Unit>) {
        for (arg in args) {
            if (!arg.contains(":")) {
                LOGGER.error("Found invalid launch argument: {}", arg)
                continue
            }

            val split = arg.split(":")
            val id = split[0].lowercase()

            LOGGER.debug("Found launch argument: {} -> {}", id, split[1])

            if (handle.containsKey(split[0].lowercase())) {
                handle[split[0].lowercase()]!!.invoke(split[1])
            }
        }
    }
}
