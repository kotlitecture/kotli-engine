package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile

/**
 * Replaces all occurrences of the given #text with provided #replacer text
 * but ONLY in lines containing #marker.
 *
 * @param marker - text which must be presented in a line to consider it for modification.
 * @param text - text to be replaced.
 * @param singleLine - if true, only the first found line will be proceeded.
 * @param replacer - text to be used.
 */
class ReplaceMarkedText(
    private val text: String,
    private val marker: String,
    private val singleLine: Boolean = false,
    private val replacer: () -> String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        val lines = file.lines
        val newText = replacer()
        logger.debug("replaceText:\n\t{}\n\t{}", text, newText)
        lines.forEachIndexed { index, line ->
            if (isMarked(file, line, marker)) {
                lines[index] = cleanup(file, line).replace(text, newText)
                if (singleLine) return
            }
        }
    }

}