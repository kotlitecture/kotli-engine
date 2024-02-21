package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Replaces the entire lines marked with #marker.
 *
 * @param marker - text which must be presented in a line to consider it for modification.
 * @param singleLine - if true, only the first found line will be proceeded.
 * @param replacer - text to replace the found line.
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