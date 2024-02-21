package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.utils.PackageUtils
import java.nio.file.Path

/**
 * Execution context for one time generation of a template.
 */
data class TemplateContext(
    val layer: Layer,
    val target: Path,
    val registry: TemplateRegistry,
    val generator: TemplateGenerator = registry.get(layer.generatorId)!!
) {

    internal val features = layer.features.associateBy { it.id }
    internal val applied = mutableMapOf<String, Feature>()
    internal val removed = mutableMapOf<String, Feature>()

    /**
     * Gets feature by its id or null if not found.
     */
    fun getFeature(id: String): Feature? = features[id]

    /**
     * Checks if processor with given #id is applied to the context.
     */
    fun isApplied(id: String): Boolean = applied.containsKey(id)

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
        val Empty = TemplateContext(
            target = Path.of("/"),
            layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generatorId = TemplateGenerator.App.getId(),
            ),
            registry = object : TemplateRegistry {
                override fun getAll(): List<TemplateGenerator> = emptyList()
                override fun get(id: String): TemplateGenerator = TemplateGenerator.App
            }
        )
    }
}