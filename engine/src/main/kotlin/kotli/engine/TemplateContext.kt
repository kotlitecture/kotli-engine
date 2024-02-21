package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateRules
import java.nio.file.Path

/**
 * Execution context for template.
 */
interface TemplateContext {

    val layer: Layer
    val layerPath: Path
    val registry: TemplateRegistry
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
     * Returns all child contexts of the given one.
     */
    fun getChildren(): List<TemplateContext>

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

    /**
     * Applies feature if it is not applied yet.
     *
     * The implementation is responsible for checking if feature is already applied or not
     * to keep the context in consistent taking into account that features can be applied in parallel.
     */
    fun onApplyFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Removes feature if it is not removed yet.
     *
     * The implementation is responsible for checking if feature is already deleted or not
     * to keep the context in consistent taking into account that features can be removed in parallel.
     */
    fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Adds new child context.
     */
    fun onAddChild(layer: Layer): TemplateContext

    companion object {
        val Empty = object : TemplateContext {

            override val layerPath: Path = Path.of("/")
            override fun getFeature(id: String): Feature? = null
            override fun onAddChild(layer: Layer): TemplateContext = this
            override val generator: TemplateGenerator = TemplateGenerator.App
            override fun onApplyFeature(id: String, action: (feature: Feature) -> Unit) = Unit
            override fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit) = Unit
            override fun onApplyRules(contextPath: String, vararg rules: TemplateRule) = Unit
            override fun getChildren(): List<TemplateContext> = emptyList()
            override fun getRules(): List<TemplateRules> = emptyList()

            override val registry: TemplateRegistry = object : TemplateRegistry {
                override fun getAll(): List<TemplateGenerator> = emptyList()
                override fun get(id: String): TemplateGenerator = TemplateGenerator.App
            }

            override val layer: Layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generatorId = TemplateGenerator.App.getId(),
            )
        }
    }
}