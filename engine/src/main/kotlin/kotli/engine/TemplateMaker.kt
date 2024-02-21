@file:OptIn(ExperimentalPathApi::class)

package kotli.engine

import org.slf4j.LoggerFactory
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteIfExists
import kotlin.io.path.deleteRecursively
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
     * Removes the entire file with all its siblings.
     */
    fun remove() {
        logger.debug("remove:\n\t{}", path)
        path.deleteRecursively()
    }

    /**
     * Replaces all occurrences of the given #text with provided #replacer text.
     *
     * @param text - text to be replaced.
     * @param replacer - text to be used.
     */
    fun replaceText(text: String, replacer: () -> String): TemplateMaker {
        val newText = replacer()
        logger.debug("replaceText:\n\t{}\n\t{}", text, newText)
        lines.forEachIndexed { index, line ->
            lines[index] = line.replace(text, newText)
        }
        return this
    }

    /**
     * Replaces all occurrences of the given #text with provided #replacer text
     * but ONLY in lines containing #marker.
     *
     * @param marker - text which must be presented in a line to consider it for modification.
     * @param text - text to be replaced.
     * @param singleLine - if true, only the first found line will be proceeded.
     * @param replacer - text to be used.
     */
    fun replaceText(marker: String, text: String, singleLine: Boolean = false, replacer: () -> String): TemplateMaker {
        val newText = replacer()
        logger.debug("replaceText:\n\t{}\n\t{}", text, newText)
        lines.forEachIndexed { index, line ->
            if (isMarked(line, marker)) {
                lines[index] = cleanup(line).replace(text, newText)
                if (singleLine) return this
            }
        }
        return this
    }

    /**
     * Replaces the entire lines marked with #marker.
     *
     * @param marker - text which must be presented in a line to consider it for modification.
     * @param singleLine - if true, only the first found line will be proceeded.
     * @param replacer - text to replace the found line.
     */
    fun replaceLine(marker: String, singleLine: Boolean = false, replacer: () -> String): TemplateMaker {
        val indexOf = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
        val newLine = replacer()
        lines.forEach { line ->
            if (isMarked(line, marker)) {
                val startIndex = line.indexOfFirst { it != ' ' }.takeIfIndex() ?: 0
                val updatedLine = "${line.substring(0, startIndex)}$newLine"
                logger.debug("replaceLine:\n\t{}\n\t{}", line, updatedLine)
                lines[indexOf] = updatedLine
                if (singleLine) return this
            }
        }
        return this
    }

    /**
     * Cleanup all lines containing given #marker by removing the marker
     * and possible cleanup prefix (which can be set in the constructor).
     *
     * @param marker - text which must be presented in a line to consider it for modification.
     * @param singleLine - if true, only the first found line will be proceeded.
     */
    fun cleanupLine(marker: String, singleLine: Boolean = false): TemplateMaker {
        lines.forEachIndexed { index, line ->
            if (isMarked(line, marker)) {
                lines[index] = cleanup(line)
                if (singleLine) return this
            }
        }
        return this
    }

    /**
     * Removes all lines containing given #marker.
     *
     * @param marker - text which must be presented in a line to consider it for modification.
     * @param singleLine - if true, only the first found line will be proceeded.
     */
    fun removeLine(marker: String, singleLine: Boolean = false): TemplateMaker {
        logger.debug("removeLine:\n\t{}", marker)
        if (singleLine) {
            val index = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
            lines.removeAt(index)
        } else {
            lines.removeIf { isMarked(it, marker) }
        }
        return this
    }

    /**
     * Replaces the entire block marked with #marker at front and #marker at the end of it.
     *
     * @param marker - text which must be presented in two lines to be considered as a block.
     * @param replacer - text to replace the found block.
     */
    fun replaceBlock(marker: String, replacer: () -> String): TemplateMaker {
        val indexOfFirst = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
        val indexOfLast = lines.indexOfLast { isMarked(it, marker) }.takeIfIndex() ?: return this
        if (indexOfFirst == indexOfLast) return this
        val firstLine = lines[indexOfFirst]
        val tab = firstLine.substring(0, firstLine.indexOfFirst { it != ' ' }.takeIfIndex() ?: 0)
        val newBlock = replacer()
        repeat(indexOfLast - indexOfFirst + 1) {
            lines.removeAt(indexOfFirst)
        }
        logger.debug("replaceBlock:\n\t{}\n\t{}", firstLine, newBlock)
        val newBlockLines = newBlock.lines().map { tab + it }
        lines.addAll(indexOfFirst, newBlockLines)
        return this
    }

    /**
     * Cleanups the block marked with #marker at front and #marker at the end of it.
     *
     * @param marker - text which must be presented in two lines to be considered as a block.
     */
    fun cleanupBlock(marker: String): TemplateMaker {
        logger.debug("cleanupBlock:\n\t{}", marker)
        lines.removeIf { isMarked(it, marker) }
        return this
    }

    /**
     * Removes the entire block marked with #marker at front and #marker at the end of it.
     *
     * @param marker - text which must be presented in two lines to be considered as a block.
     */
    fun removeBlock(marker: String): TemplateMaker {
        val indexOfFirst = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
        val indexOfLast = lines.indexOfLast { isMarked(it, marker) }.takeIfIndex() ?: return this
        if (indexOfFirst == indexOfLast) return this
        logger.debug("removeBlock:\n\t{}", marker)
        repeat(indexOfLast - indexOfFirst + 1) {
            lines.removeAt(indexOfFirst)
        }
        return this
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

    private fun isMarked(line: String, marker: String): Boolean {
        return line.contains(marker, ignoreCase = true)
    }

    private fun cleanup(line: String): String {
        val startIndex = line.indexOfAny(markerCleanupPrefixes).takeIfIndex() ?: return line
        val newLine = line.substring(0, startIndex).trimEnd()
        logger.debug("cleanupLine:\n\t{}\n\t{}", line, newLine)
        return newLine
    }

}

fun Int.takeIfIndex(): Int? = this.takeIf { it != -1 }