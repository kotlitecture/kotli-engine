@file:OptIn(ExperimentalPathApi::class)

package kotli.engine

import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.readLines
import kotlin.io.path.writeText

/**
 * Simplified template engine operating on the provided file path entirely.
 * It is not required to use this engine, so any other normal engine can be used.
 *
 * path - path to a file to proceed with changes.
 * markerCleanupPrefixes - list of possible prefixes, specific for marked lines
 * (usually, when any line is marked, the marker is distinguished from the logical block
 * using one of the given prefixes (usually, symbols of comment, like '//' or '#').
 */
class TemplateMaker(
    private val path: Path,
    private val markerCleanupPrefixes: List<String> = listOf("//", "#")
) {

    companion object {
        private val logger = LoggerFactory.getLogger(TemplateMaker::class.java)
    }

    private val lines by lazy {
        if (path.exists()) {
            path.readLines().toMutableList()
        } else {
            mutableListOf()
        }
    }

    /**
     * Applies all changes to the file.
     */
    fun apply() {
        logger.debug("apply :: {} -> {}", path, path.exists())
        if (lines.isNotEmpty()) {
            val content = lines.joinToString("\n")
            path.writeText(content)
        } else if (path.exists()) {
            path.deleteIfExists()
        }
    }

}