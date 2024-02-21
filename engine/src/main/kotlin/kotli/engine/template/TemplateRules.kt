package kotli.engine.template

import java.nio.file.Path

data class TemplateRules(
    val filePath: Path,
    val rules: List<TemplateRule>
)