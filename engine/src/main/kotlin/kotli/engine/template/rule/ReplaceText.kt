package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile

/**
 * Replaces all occurrences of the given #text with provided #replacer text.
 *
 * @param text - text to be replaced.
 * @param replacer - text to be used.
 */
class ReplaceText(
    private val text: String,
    private val replacer: () -> String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val newText = replacer()
        logger.debug("replaceText:\n\t{}\n\t{}", text, newText)
        file.lines.forEachIndexed { index, line ->
            file.lines[index] = line.replace(text, newText)
        }
    }

}