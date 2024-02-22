package kotli.engine.template

import java.nio.file.Path

/**
 * Describes a set of #rules which will be applied to the given #filePath.
 *
 * @param filePath The path to the file relative to the template root context.
 * @param rules The list of rules to be applied to the file.
 */
data class TemplateRules(
    val filePath: Path,
    val rules: List<TemplateRule>
)