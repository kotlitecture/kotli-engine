package kotli.engine.template

import java.nio.file.Path
import kotlin.io.path.*

/**
 * Describes any file of the template.
 *
 * @param path The path to the file relative to the template root context.
 * @param markerSeparators Separators used to separate markers from logical blocks so the markers can be extracted and removed properly from output text.
 */
data class TemplateFile(
        val path: Path,
        val markerSeparators: List<String>
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
}