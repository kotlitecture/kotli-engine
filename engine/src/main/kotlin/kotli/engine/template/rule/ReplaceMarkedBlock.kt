package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotli.engine.extensions.takeIfIndex

/**
 * Replaces the entire block marked with a specific marker at the beginning and end of it.
 *
 * @param marker The text that must be presented in two lines to be considered as a block.
 * @param replacer A function providing the text to replace the found block.
 */
class ReplaceMarkedBlock(
    private val marker: String,
    private val replacer: () -> String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        val indexOfFirst = lines.indexOfFirst { isMarked(file, it, marker) }.takeIfIndex() ?: return
        val indexOfLast = lines.indexOfLast { isMarked(file, it, marker) }.takeIfIndex() ?: return
        if (indexOfFirst == indexOfLast) return
        val firstLine = lines[indexOfFirst]
        val tab = firstLine.substring(0, firstLine.indexOfFirst { it != ' ' }.takeIfIndex() ?: 0)
        val newBlock = replacer()
        repeat(indexOfLast - indexOfFirst + 1) {
            lines.removeAt(indexOfFirst)
        }
        logger.debug("replaceBlock:\n\t{}\n\t{}", firstLine, newBlock)
        val newBlockLines = newBlock.lines().map { tab + it }
        lines.addAll(indexOfFirst, newBlockLines)
    }

}