package kotli.engine.template

import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

/**
 * Describes any file of the template.
 *
 * @param path The path to the file relative to the template root context.
 * @param markers Markers used to mark lines or blocks for their usage by [kotli.engine.template.TemplateRule].
 */
data class TemplateFile(
    val path: Path,
    val markers: List<String> = MARKERS
) {
    val lines by lazy {
        if (path.exists()) {
            path.readLines().toMutableList()
        } else {
            mutableListOf()
        }
    }

    fun write() {
        if (!path.exists()) return
        if (path.isDirectory()) return
        if (lines.isNotEmpty()) {
            path.writeLines(lines)
        } else {
            path.deleteIfExists()
        }
    }

    companion object {
        val MARKERS = listOf("//", "#")
    }
}