package kotli.engine.model

/**
 * Represents a feature that can be configured within layers with predefined integrations,
 * technical solutions, and business flows in Kotli.
 *
 * @param id The unique identifier of the feature.
 * @param attributes The optional attributes associated with the feature.
 */
data class Feature(
    val id: String,
    val attributes: Map<String, String> = emptyMap()
)