package kotli.engine.model

/**
 * Layers can be configured with predefined integrations, technical solutions and business flows.
 * In terms of Kotli, every atomic integration, technical solution or business flow is a feature.
 */
data class Feature(
    val id: String,
    val attributes: Map<String, String> = emptyMap()
)