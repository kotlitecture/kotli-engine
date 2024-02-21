package kotli.engine.template.rule

import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateFile
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

/**
 * Removes the entire file with all its siblings.
 */
@OptIn(ExperimentalPathApi::class)
class RemoveFile : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        file.path.deleteRecursively()
    }

}