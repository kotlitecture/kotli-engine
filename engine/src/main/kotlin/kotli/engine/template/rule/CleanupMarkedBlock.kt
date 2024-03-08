package kotli.engine.template.rule

import kotli.engine.template.FileRule
import kotli.engine.template.TemplateFile

/**
 * Cleans up the block marked with a specific marker at the beginning and end of it.
 *
 * @param marker The text that must be presented in two lines to be considered as a block.
 */
data class CleanupMarkedBlock(
    private val marker: String
) : FileRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        logger.debug("cleanupBlock:\n\t{}", marker)
        lines.removeIf { isMarked(file, it, marker) }
    }

}