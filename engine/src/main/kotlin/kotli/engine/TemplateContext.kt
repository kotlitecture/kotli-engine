package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.utils.PackageUtils
import java.nio.file.Path

/**
 * Execution context for one time generation of a template.
 */
interface TemplateContext {

    val layer: Layer
    val target: Path
    val registry: TemplateRegistry
    val generator: TemplateGenerator

    /**
     * Finds feature by its id in the provided layer or null if not found.
     */
    fun findFeature(id: String): Feature?

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
     * Creates new child context for the given layer.
     */
    fun createChildContext(layer: Layer): TemplateContext

    /**
     * Applies template engine to the #contextPath relative to the root of the target folder.
     */
    fun apply(contextPath: String, block: TemplateMaker.() -> Unit) {
        val maker = TemplateMaker(target.resolve(contextPath))
        maker.block()
        maker.apply()
    }

    /**
     * Renames given #oldPackage to #newPackage found in #contextPath.
     */
    fun rename(contextPath: String, oldPackage: String, newPackage: String) {
        PackageUtils.rename(target.resolve(contextPath), oldPackage, newPackage)
    }

    companion object {
        val Empty = object : TemplateContext {

            override val target: Path = Path.of("/")
            override fun findFeature(id: String): Feature? = null
            override val generator: TemplateGenerator = TemplateGenerator.App
            override fun createChildContext(layer: Layer): TemplateContext = this
            override fun onApplyFeature(id: String, action: (feature: Feature) -> Unit) = Unit
            override fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit) = Unit

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