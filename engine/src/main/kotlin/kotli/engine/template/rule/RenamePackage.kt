package kotli.engine.template.rule

import kotli.engine.template.TemplateFile
import kotli.engine.template.TemplateRule
import kotli.engine.utils.PackageUtils

/**
 * Renames the given package from #oldPackage to #newPackage found in #contextPath.
 *
 * @param oldPackage The package to be renamed.
 * @param newPackage The new name for the package.
 */
class RenamePackage(
    private val oldPackage: String,
    private val newPackage: String
) : TemplateRule() {

    override fun doApply(file: TemplateFile) {
        PackageUtils.rename(file.path, oldPackage, newPackage)
    }

}