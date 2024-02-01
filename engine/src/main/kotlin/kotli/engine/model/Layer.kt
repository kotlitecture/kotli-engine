package kotli.engine.model

import kotli.engine.ITemplateGenerator

/**
 * Metadata of a specific layer with all configured features and attributes.
 */
data class Layer(
    val id: String,
    val name: String,
    val namespace: String,
    val description: String? = null,
    val generator: ITemplateGenerator,
    val layers: List<Layer> = emptyList(),
    val features: List<Feature> = emptyList()
)