package kotli.engine.template

data class VersionCatalogRules(override val rules: List<FileRule>) : FileRules("gradle/libs.versions.toml", rules)