package kotli.engine.model

import kotli.engine.LayerType
import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Represents a predefined set of layer types.
 *
 * @param id The unique identifier of the layer type.
 * @param order The order in which the layer type should be processed.
 */
enum class LayerTypes(
    private val id: String,
    private val order: Int
) : LayerType {

    App("app", 0),
    Multiplatform("multiplatform", 1),
    Android("android", 2),
    Backend("backend", 3),
    IOS("ios", 4),
    Web("web", 5),
    Desktop("desktop", 6),
    Unspecified("unspecified", 7)

    ;

    override fun getId(): String = id
    override fun getOrder(): Int = order
    override fun getIcon(): URL? = ResourceUtils.get(this, "layer_type_${id}.svg")
    override fun getTitle(): String? = ResourceUtils.getAsString(this, "layer_type_${id}_title.md")
    override fun getDescription(): String? = ResourceUtils.getAsString(this, "layer_type_${id}_description.md", true)

}