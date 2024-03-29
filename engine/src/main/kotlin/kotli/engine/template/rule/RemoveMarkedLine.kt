package kotli.engine.template.rule

import kotli.engine.extensions.takeIfIndex
import kotli.engine.template.FileRule
import kotli.engine.template.TemplateFile

/**
 * Removes all lines containing a given marker.
 *
 * @param marker The text that must be present in a line to consider it for modification.
 * @param singleLine If true, only the first found line will be processed.
 */
data class RemoveMarkedLine(
    private val marker: String,
    private val singleLine: Boolean = false
) : FileRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        if (singleLine) {
            val index = lines.indexOfFirst { isMarked(file, it, marker) }.takeIfIndex() ?: return
            lines.removeAt(index)
        } else {
            lines.removeIf { isMarked(file, it, marker) }
        }
    }

}