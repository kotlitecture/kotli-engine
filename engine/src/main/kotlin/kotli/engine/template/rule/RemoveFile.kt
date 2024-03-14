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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}