package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateRules
import org.slf4j.LoggerFactory
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Default execution context.
 */
class DefaultTemplateContext(
    override val layer: Layer,
    override val layerPath: Path,
    private val registry: TemplateRegistry
) : TemplateContext {

    private val logger = LoggerFactory.getLogger(DefaultTemplateContext::class.java)

    override val generator: TemplateGenerator = registry.get(layer.generatorId)!!

    private val children = ConcurrentHashMap<String, TemplateContext>()
    private val features = layer.features.associateBy { it.id }
    private val applied = ConcurrentHashMap<String, Feature>()
    private val removed = ConcurrentHashMap<String, Feature>()
    private val rules = CopyOnWriteArrayList<TemplateRules>()

    override fun getRules(): List<TemplateRules> = rules

    override fun getFeature(id: String): Feature? = features[id]

    override fun getChildren(): List<TemplateContext> = children.values.toList()

    override fun onApplyRules(contextPath: String, vararg rules: TemplateRule) {
        val filePath = layerPath.resolve(contextPath)
        this.rules.add(TemplateRules(filePath, rules.toList()))
    }

    override fun onApplyFeature(id: String, action: (feature: Feature) -> Unit) {
        applied.computeIfAbsent(id) {
            val feature = getFeature(id) ?: Feature(id)
            action(feature)
            feature
        }
    }

    override fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit) {
        if (!applied.containsKey(id)) {
            removed.computeIfAbsent(id) {
                val feature = getFeature(id) ?: Feature(id)
                action(feature)
                feature
            }
        }
    }

    override fun onAddChild(layer: Layer): TemplateContext? {
        val name = layer.name
        if (registry.get(layer.generatorId) == null) {
            logger.warn("found layer with unknown generator id :: {}", layer.generatorId)
            return null
        }
        if (children.containsKey(name)) {
            throw IllegalStateException("multiple children found with the same id $name")
        }
        val child = DefaultTemplateContext(
            layerPath = layerPath.resolve(layer.name),
            registry = registry,
            layer = layer
        )
        children[name] = child
        return child
    }
}