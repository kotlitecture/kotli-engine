package kotli.flow

import kotli.engine.TemplateContext
import kotli.engine.TemplateGenerator
import kotli.engine.TemplateMaker
import kotli.engine.TemplateRegistry
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.utils.PackageUtils
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

/**
 * Default execution context.
 */
data class DefaultTemplateContext(
    override val layer: Layer,
    override val target: Path,
    override val registry: TemplateRegistry,
    override val generator: TemplateGenerator = registry.get(layer.generatorId)!!
) : TemplateContext {

    private val features = layer.features.associateBy { it.id }
    private val applied = ConcurrentHashMap<String, Feature>()
    private val removed = ConcurrentHashMap<String, Feature>()

    override fun findFeature(id: String): Feature? = features[id]

    override fun onApplyFeature(id: String, action: (feature: Feature) -> Unit) {
        applied.computeIfAbsent(id) {
            val feature = findFeature(id) ?: Feature(id)
            action(feature)
            feature
        }
    }

    override fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit) {
        if (!applied.containsKey(id)) {
            removed.computeIfAbsent(id) {
                val feature = findFeature(id) ?: Feature(id)
                action(feature)
                feature
            }
        }
    }

    override fun createChildContext(layer: Layer): TemplateContext {
        return DefaultTemplateContext(
            target = target.resolve(layer.name),
            registry = registry,
            layer = layer
        )
    }

    override fun apply(contextPath: String, block: TemplateMaker.() -> Unit) {
        val maker = TemplateMaker(target.resolve(contextPath))
        maker.block()
        maker.apply()
    }

    override fun rename(contextPath: String, oldPackage: String, newPackage: String) {
        PackageUtils.rename(target.resolve(contextPath), oldPackage, newPackage)
    }
}