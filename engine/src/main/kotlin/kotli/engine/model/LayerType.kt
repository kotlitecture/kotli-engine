package kotli.engine.model

import kotli.engine.ILayerType
import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Predefined set of ILayerType.
 */
enum class LayerType(private val code: String, private val order: Int = 0) : ILayerType {

    App("app", 0),
    Backend("backend", 1),
    Android("android", 2),
    IOS("ios", 3),
    Web("web", 4),
    Desktop("desktop", 5),
    Multiplatform("multiplatform", 6),
    Unspecified("unspecified", 7)

    ;

    override fun getId(): String = code
    override fun getOrder(): Int = order
    override fun getIcon(): URL? = ResourceUtils.get(this, "layer_type_${code}.svg")
    override fun getTitle(): String? = ResourceUtils.getAsString(this, "layer_type_${code}_title.md")
    override fun getDescription(): String? = ResourceUtils.getAsString(this, "layer_type_${code}_description.md")

}