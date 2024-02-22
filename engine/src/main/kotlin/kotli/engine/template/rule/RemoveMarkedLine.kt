package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Removes all lines containing a given marker.
 *
 * @param marker The text that must be present in a line to consider it for modification.
 * @param singleLine If true, only the first found line will be processed.
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