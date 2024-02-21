package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile

/**
 * Cleanups the block marked with #marker at front and #marker at the end of it.
 *
 * @param marker - text which must be presented in two lines to be considered as a block.
 */
class CleanupMarkedBlock(
    private val marker: String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        logger.debug("cleanupBlock:\n\t{}", marker)
        lines.removeIf { isMarked(file, it, marker) }
    }

}