package kotli.engine.template.rule

import kotli.engine.template.TemplateFile
import kotli.engine.template.TemplateRule
import kotli.engine.utils.PackageUtils

/**
 * Renames given #oldPackage to #newPackage found in #contextPath.
 */
class RenamePackage(
    private val oldPackage: String,
    private val newPackage: String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        PackageUtils.rename(file.path, oldPackage, newPackage)
    }

}