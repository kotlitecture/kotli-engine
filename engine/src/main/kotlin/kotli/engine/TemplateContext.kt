package kotli.engine

import kotli.engine.model.Layer
import java.nio.file.Path

/**
 * Execution context for one time generation of a template.
 *
 * Once this context is executed
 */
data class TemplateContext(
    val layer: Layer,
    val target: Path
) {

    private val applied: MutableMap<String, IFeatureProcessor> = mutableMapOf()

    /**
     * Checks if processor with given #id is applied to the context.
     */
    fun isApplied(id: String): Boolean = applied.containsKey(id)

    /**
     * Returns all applied processors.
     */
    fun getApplied(): Collection<IFeatureProcessor> = applied.values

    /**
     * Applies given processor to the context.
     */
    internal fun apply(processor: IFeatureProcessor, block: () -> Unit) {
        if (applied.putIfAbsent(processor.getId(), processor) == null) {
            block()
        }
    }

    /**
     * Applies template engine to the #contextPath relative to the root of the target folder.
     */
    fun apply(contextPath: String, block: TemplateMaker.() -> Unit) {
        val maker = TemplateMaker(target.resolve(contextPath))
        maker.block()
        maker.apply()
    }

    /**
     * Applies template engine to the 'gradle/libs.versions.toml' in the root of the target folder.
     */
    fun applyVersionCatalog(block: TemplateMaker.() -> Unit) {
        apply("gradle/libs.versions.toml", block)
    }

    companion object {
        val Empty = TemplateContext(
            target = Path.of("/"),
            layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generator = ITemplateGenerator.App
            )
        )
    }
}