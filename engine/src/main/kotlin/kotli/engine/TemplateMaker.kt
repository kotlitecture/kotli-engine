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

private val logger = LoggerFactory.getLogger(TemplateMaker::class.java)

class TemplateMaker(private val path: Path) {

    companion object {
        private val LOG = LoggerFactory.getLogger(TemplateMaker::class.java)
    }

    private val lines by lazy {
        if (path.exists()) path.readLines().toMutableList() else mutableListOf()
    }

    fun remove() {
        LOG.debug("remove:\n\t{}", path)
        path.deleteRecursively()
    }

    fun replaceText(text: String, replacer: () -> String): TemplateMaker {
        val newText = replacer()
        LOG.debug("replaceText:\n\t{}\n\t{}", text, newText)
        lines.forEachIndexed { index, line ->
            lines[index] = line.replace(text, newText)
        }
        return this
    }

    fun replaceText(marker: String, text: String, replacer: () -> String): TemplateMaker {
        val newText = replacer()
        LOG.debug("replaceText:\n\t{}\n\t{}", text, newText)
        lines.forEachIndexed { index, line ->
            if (isMarked(marker, line)) {
                lines[index] = cleanup(line).replace(text, newText)
            }
        }
        return this
    }

    fun replaceLine(marker: String, replacer: () -> String): TemplateMaker {
        val indexOf = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
        val line = lines[indexOf]
        val startIndex = line.indexOfFirst { it != ' ' }.takeIfIndex() ?: 0
        val newLine = replacer()
        val updatedLine = "${line.substring(0, startIndex)}$newLine"
        LOG.debug("replaceLine:\n\t{}\n\t{}", line, updatedLine)
        lines[indexOf] = updatedLine
        return this
    }

    fun cleanupLine(marker: String): TemplateMaker {
        lines.forEachIndexed { index, line ->
            if (isMarked(line, marker)) {
                lines[index] = cleanup(line)
            }
        }
        return this
    }

    fun removeLine(marker: String): TemplateMaker {
        LOG.debug("removeLine:\n\t{}", marker)
        lines.removeIf { isMarked(it, marker) }
        return this
    }

    fun removeLine(isMarked: (line: String) -> Boolean): TemplateMaker {
        LOG.debug("removeLine:\n\t{}", isMarked)
        lines.removeIf { isMarked(it) }
        return this
    }

    fun cleanupBlock(marker: String): TemplateMaker {
        LOG.debug("cleanupBlock:\n\t{}", marker)
        lines.removeIf { isMarked(it, marker) }
        return this
    }

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
        LOG.debug("replaceBlock:\n\t{}\n\t{}", firstLine, newBlock)
        val newBlockLines = newBlock.lines().map { tab + it }
        lines.addAll(indexOfFirst, newBlockLines)
        return this
    }

    fun removeBlock(marker: String): TemplateMaker {
        val indexOfFirst = lines.indexOfFirst { isMarked(it, marker) }.takeIfIndex() ?: return this
        val indexOfLast = lines.indexOfLast { isMarked(it, marker) }.takeIfIndex() ?: return this
        if (indexOfFirst == indexOfLast) return this
        LOG.debug("removeBlock:\n\t{}", marker)
        repeat(indexOfLast - indexOfFirst + 1) {
            lines.removeAt(indexOfFirst)
        }
        return this
    }

    internal fun apply() {
        logger.debug("apply :: {} -> {}", path, path.exists())
        if (lines.isNotEmpty()) {
            val content = lines.joinToString("\n")
            path.writeText(content)
        } else if (path.exists()) {
            path.deleteIfExists()
        }
    }

    private fun isMarked(line: String, marker: String): Boolean {
        return line.contains(marker)
    }

    private fun cleanup(line: String): String {
        val startIndex = line.indexOfAny(listOf("//", "#")).takeIfIndex() ?: return line
        val newLine = line.substring(0, startIndex).trimEnd()
        LOG.debug("cleanupLine:\n\t{}\n\t{}", line, newLine)
        return newLine
    }

}

fun Int.takeIfIndex(): Int? = this.takeIf { it != -1 }