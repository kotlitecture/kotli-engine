package kotli.engine

import kotli.engine.extensions.path
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.FileRule
import kotli.engine.template.FileRules
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Default execution context.
 */
class DefaultTemplateContext(
    override val layer: Layer,
    override val contextPath: String,
    private val registry: TemplateRegistry
) : TemplateContext {

    private val logger = LoggerFactory.getLogger(DefaultTemplateContext::class.java)

    override val processor: TemplateProcessor = registry.get(layer.processorId)!!

    private val children = ConcurrentHashMap<String, TemplateContext>()
    private val features = layer.features.associateBy { it.id }
    private val applied = ConcurrentHashMap<String, Feature>()
    private val removed = ConcurrentHashMap<String, Feature>()
    private val rules = CopyOnWriteArrayList<FileRules>()

    override fun getRules(): List<FileRules> = rules

    override fun getFeature(id: String): Feature? = features[id]

    override fun getChildren(): List<TemplateContext> = children.values.toList()

    override fun getAppliedFeatures(): List<Feature> = applied.values.toList()

    override fun getRemovedFeatures(): List<Feature> = removed.values.toList()

    override fun onApplyRules(contextPath: String, vararg rules: FileRule) {
        onApplyRules(FileRules(contextPath, rules.toList()))
    }

    override fun onApplyRules(rules: FileRules) {
        this.rules.add(rules)
    }

    override fun onApplyFeature(id: String, action: (feature: Feature) -> Unit) {
        applied.computeIfAbsent(id) {
            logger.debug("onApplyFeature :: {}", it)
            val feature = getFeature(id) ?: Feature(id)
            action(feature)
            feature
        }
    }

    override fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit) {
        if (!applied.containsKey(id)) {
            removed.computeIfAbsent(id) {
                logger.debug("onRemoveFeature :: {}", it)
                val feature = getFeature(id) ?: Feature(id)
                action(feature)
                feature
            }
        }
    }

    override fun onAddChild(layer: Layer): TemplateContext? {
        val name = layer.name
        if (registry.get(layer.processorId) == null) {
            logger.warn("found layer with unknown processor id :: {}", layer.processorId)
            return null
        }
        if (children.containsKey(name)) {
            throw IllegalStateException("multiple children found with the same id $name")
        }
        val child = DefaultTemplateContext(
            contextPath = path(contextPath, layer.name),
            registry = registry,
            layer = layer
        )
        children[name] = child
        return child
    }
}