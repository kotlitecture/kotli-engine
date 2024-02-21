package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Removes all lines containing given #marker.
 *
 * @param marker - text which must be presented in a line to consider it for modification.
 * @param singleLine - if true, only the first found line will be proceeded.
 */
class RemoveMarkedLine(
    private val marker: String,
    private val singleLine: Boolean = false
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        logger.debug("removeLine:\n\t{}", marker)
        if (singleLine) {
            val index = lines.indexOfFirst { isMarked(file, it, marker) }.takeIfIndex() ?: return
            lines.removeAt(index)
        } else {
            lines.removeIf { isMarked(file, it, marker) }
        }
    }

}