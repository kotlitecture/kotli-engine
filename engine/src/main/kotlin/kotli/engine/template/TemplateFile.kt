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

    val linesDelegate = lazy {
        if (path.exists()) {
            path.readLines().toMutableList()
        } else {
            mutableListOf()
        }
    }

    val lines by linesDelegate

    /**
     * Replaces the current text with the specified text.
     *
     * @param text The new text to set.
     */
    fun setText(text: String) {
        lines.clear()
        lines.addAll(text.lines())
    }

    /**
     * Replaces the current text with the text generated by the provided block.
     *
     * @param block A function that takes the current text and returns the new text.
     */
    fun setText(block: (source: String) -> String) {
        val newText = block(lines.joinToString("\n"))
        lines.clear()
        lines.addAll(newText.lines())
    }

}