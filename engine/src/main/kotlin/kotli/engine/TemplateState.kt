package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateRules
import java.nio.file.Path

/**
 * Read-only state of execution context.
 */
interface TemplateState {

    val layer: Layer
    val layerPath: Path
    val generator: TemplateGenerator

    /**
     * Returns all available rules to be applied.
     */
    fun getRules(): List<TemplateRules>

    /**
     * Finds feature by its id in the provided layer or null if not found.
     */
    fun getFeature(id: String): Feature?

    /**
     * Returns all children contexts of the given one.
     */
    fun getChildren(): List<TemplateState>

    /**
     * Adds rules to be applied for a given file #contextPath relative to the layer path.
     */
    fun onApplyRules(contextPath: String, vararg rules: TemplateRule)

    /**
     * Adds rules to be applied for the 'gradle/libs.versions.toml' relative to the layer path.
     */
    fun onApplyVersionCatalogRules(vararg rules: TemplateRule) {
        onApplyRules("gradle/libs.versions.toml", *rules)
    }

    companion object {
        val Empty = object : TemplateState {
            override fun onApplyRules(contextPath: String, vararg rules: TemplateRule) = Unit
            override fun onApplyVersionCatalogRules(vararg rules: TemplateRule) = Unit
            override val generator: TemplateGenerator = TemplateGenerator.App
            override fun getChildren(): List<TemplateState> = emptyList()
            override fun getRules(): List<TemplateRules> = emptyList()
            override fun getFeature(id: String): Feature? = null
            override val layerPath: Path = Path.of("/")
            override val layer: Layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generatorId = TemplateGenerator.App.getId(),
            )
        }
    }
}