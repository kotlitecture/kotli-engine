package kotli.engine.model

import kotli.engine.IFeatureType
import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Predefined set of IFeatureType.
 */
enum class FeatureType(private val code: String, private val order: Int = 0) : IFeatureType {

    Build("build", 1),
    DataSource("datasource", 2),
    Workflow("workflow", 3),
    Appearance("appearance", 4),
    UI("ui", 5),
    Quality("quality", 6),
    Testing("testing", 7),
    Documentation("documentation", 8),
    Transitive("transitive", Int.MAX_VALUE) {
        override fun isInternal(): Boolean = true
    },

    ;

    override fun getId(): String = code
    override fun getOrder(): Int = order
    override fun getIcon(): URL? = ResourceUtils.get(this, "feature_type_${code}.svg")
    override fun getTitle(): String? = ResourceUtils.getAsString(this, "feature_type_${code}_title.md")
    override fun getDescription(): String? = ResourceUtils.getAsString(this, "feature_type_${code}_description.md")

}