package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Removes the entire block marked with #marker at front and #marker at the end of it.
 *
 * @param marker - text which must be presented in two lines to be considered as a block.
 */
class RemoveMarkedBlock(
    private val marker: String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        val indexOfFirst = lines.indexOfFirst { isMarked(file, it, marker) }.takeIfIndex() ?: return
        val indexOfLast = lines.indexOfLast { isMarked(file, it, marker) }.takeIfIndex() ?: return
        if (indexOfFirst == indexOfLast) return
        logger.debug("removeBlock:\n\t{}", marker)
        repeat(indexOfLast - indexOfFirst + 1) {
            lines.removeAt(indexOfFirst)
        }
    }

}