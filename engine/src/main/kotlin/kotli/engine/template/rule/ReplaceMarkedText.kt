package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile

/**
 * Replaces all occurrences of the given #text with the provided #replacer text
 * but ONLY in lines containing #marker.
 *
 * @param text The text to be replaced.
 * @param marker The text that must be present in a line to consider it for modification.
 * @param singleLine If true, only the first found line will be processed.
 * @param replacer A function providing the text to be used as a replacement.
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