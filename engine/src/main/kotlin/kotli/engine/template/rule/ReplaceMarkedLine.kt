package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Replaces the entire lines marked with a specific marker.
 *
 * @param marker The text that must be present in a line to consider it for modification.
 * @param singleLine If true, only the first found line will be processed.
 * @param replacer A function providing the text to replace the found line.
 */
class ReplaceMarkedLine(
    private val marker: String,
    private val singleLine: Boolean = false,
    private val replacer: () -> String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        val indexOf = lines.indexOfFirst { isMarked(file, it, marker) }.takeIfIndex() ?: return
        val newLine = replacer()
        lines.forEach { line ->
            if (isMarked(file, line, marker)) {
                val startIndex = line.indexOfFirst { it != ' ' }.takeIfIndex() ?: 0
                val updatedLine = "${line.substring(0, startIndex)}$newLine"
                logger.debug("replaceLine:\n\t{}\n\t{}", line, updatedLine)
                lines[indexOf] = updatedLine
                if (singleLine) return
            }
        }
    }

}