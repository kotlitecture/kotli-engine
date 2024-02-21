package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile

/**
 * Cleanup all lines containing given #marker by removing the marker
 * and possible cleanup prefix (which can be set in the constructor).
 *
 * @param marker - text which must be presented in a line to consider it for modification.
 * @param singleLine - if true, only the first found line will be proceeded.
 */
class CleanupMarkedLine(
    private val marker: String,
    private val singleLine: Boolean = false
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        lines.forEachIndexed { index, line ->
            if (isMarked(file, line, marker)) {
                lines[index] = cleanup(file, line)
                if (singleLine) return
            }
        }
    }

}