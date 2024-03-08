package kotli.engine.template.rule

import kotli.engine.template.FileRule
import kotli.engine.template.TemplateFile

/**
 * Replaces all occurrences of the given #text with the provided #replacer text.
 *
 * @param text The text to be replaced.
 * @param replacer A function providing the text to be used as a replacement.
 */
data class ReplaceText(
    private val text: String,
    private val replacer: String
) : FileRule() {

    override fun doApply(file: TemplateFile) {
        val newText = replacer
        logger.debug("replaceText:\n\t{}\n\t{}", text, newText)
        file.lines.forEachIndexed { index, line ->
            file.lines[index] = line.replace(text, newText)
        }
    }

}