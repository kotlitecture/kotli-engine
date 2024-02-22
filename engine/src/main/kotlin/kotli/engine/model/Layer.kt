package kotli.engine.model

/**
 * Represents the metadata of a specific layer with all configured features and attributes.
 *
 * @param id The unique identifier of the layer.
 * @param name The name representing the root folder of the generated layer.
 * @param namespace The package name, bundle id, or application id, depending on the context.
 * @param processorId The id of the processor used to resolve in the template registry.
 * @param description A brief description of the layer.
 * @param layers The list of child layers, if applicable.
 * @param features The list of features configured for the layer.
 */
data class Layer(
    val id: String,
    val name: String,
    val namespace: String,
    val processorId: String,
    val description: String? = null,
    val layers: List<Layer> = emptyList(),
    val features: List<Feature> = emptyList()
)