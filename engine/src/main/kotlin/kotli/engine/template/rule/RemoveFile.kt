package kotli.engine.template.rule

import kotli.engine.template.FileRule
import kotli.engine.template.TemplateFile
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively

/**
 * Removes the entire file with all its siblings.
 *
 * Note: This rule uses experimental features from the `kotlin.io.path` package.
 */
@OptIn(ExperimentalPathApi::class)
class RemoveFile : FileRule() {

    override fun doApply(file: TemplateFile) {
        file.path.deleteRecursively()
    }

}