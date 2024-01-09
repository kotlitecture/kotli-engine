package kotli.engine.model

import kotli.engine.ITemplateGenerator

data class Layer(
    val id: String,
    val name: String,
    val namespace: String,
    val description: String? = null,
    val generator: ITemplateGenerator,
    val layers: List<Layer> = emptyList(),
    val features: List<Feature> = emptyList()
)