package kotli.engine.model

import kotli.engine.ILayerType
import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Predefined set of ILayerType.
 */
enum class LayerType(
    override val id: String,
    override val order: Int
) : ILayerType {

    App("app", 0),
    Backend("backend", 1),
    Android("android", 2),
    IOS("ios", 3),
    Web("web", 4),
    Desktop("desktop", 5),
    Multiplatform("multiplatform", 6),
    Unspecified("unspecified", 7)

    ;

    override fun getIcon(): URL? = ResourceUtils.get(this, "layer_type_${id}.svg")
    override fun getTitle(): String? = ResourceUtils.getAsString(this, "layer_type_${id}_title.md")
    override fun getDescription(): String? = ResourceUtils.getAsString(this, "layer_type_${id}_description.md")

}