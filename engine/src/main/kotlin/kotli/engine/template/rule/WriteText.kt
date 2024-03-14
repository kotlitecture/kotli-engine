package kotli.engine.template.rule

import kotli.engine.template.FileRule
import kotli.engine.template.TemplateFile

/**
 * Writes the provided [text] to a file, replacing all of its existing content.
 *
 * @param text the text to be written to the file
 */
data class WriteText(
    private val text: String
) : FileRule() {

    override fun doApply(file: TemplateFile) {
        file.setText(text)
    }

}