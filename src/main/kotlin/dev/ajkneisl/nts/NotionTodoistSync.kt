package dev.ajkneisl.nts

import dev.ajkneisl.nts.impl.notion.Widget
import dev.ajkneisl.nts.sync.Sync
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object NotionTodoistSync {
    private val LOGGER: Logger = LoggerFactory.getLogger(this.javaClass)

    var NOTION_API_KEY = ""
    var TODOIST_API_KEY = ""
    var BLOCK_ID = ""
    val BLOCK_TIED_PROJECT = hashMapOf<String, String>()
    var BLOCK_USE_PROJECT_SPECIFIC = false
    var REFRESH_INTERVAL: Long = 1000 * 60 * 5

    suspend fun main(args: Array<String>) {
        parseArgs(
            args,
            hashMapOf(
                "--use_block" to { BLOCK_ID = it },
                "--project_specific" to { BLOCK_USE_PROJECT_SPECIFIC = it.toBoolean() },
                "--tie_project" to
                    {
                        val split = it.split("|")
                        BLOCK_TIED_PROJECT[split[0]] = split[1]
                    },
                "--notion" to { NOTION_API_KEY = it },
                "--todoist" to { TODOIST_API_KEY = it },
                "--refresh" to { REFRESH_INTERVAL = it.toLong() },
                "--bgcolor" to Widget::handleColorArgument
            )
        )

        Sync.syncTasks(true)
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
