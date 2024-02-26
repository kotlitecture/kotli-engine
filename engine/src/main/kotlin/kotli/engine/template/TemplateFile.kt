package kotli.engine.template

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

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

    fun setText(text: String) {
        lines.clear()
        lines.addAll(text.lines())
    }

}