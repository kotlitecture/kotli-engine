package kotli.engine.model

/**
 * Metadata of a specific layer with all configured features and attributes.
 */
data class Layer(
    val id: String,
    val name: String,
    val namespace: String,
    val generatorId: String,
    val description: String? = null,
    val layers: List<Layer> = emptyList(),
    val features: List<Feature> = emptyList()
)