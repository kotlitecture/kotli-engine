package kotli.flow

import kotli.engine.TemplateContext
import kotli.engine.TemplateGenerator
import kotli.engine.TemplateRegistry
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateRule
import kotli.engine.template.TemplateRules
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Default execution context.
 */
class DefaultTemplateContext(
    override val layer: Layer,
    override val layerPath: Path,
    override val registry: TemplateRegistry,
    override val generator: TemplateGenerator = registry.get(layer.generatorId)!!
) : TemplateContext {

    private val children = mutableMapOf<String, TemplateContext>()
    private val features = layer.features.associateBy { it.id }
    private val applied = ConcurrentHashMap<String, Feature>()
    private val removed = ConcurrentHashMap<String, Feature>()
    private val rules = CopyOnWriteArrayList<TemplateRules>()

    override fun getRules(): List<TemplateRules> = rules

    override fun getFeature(id: String): Feature? = features[id]

    override fun getChildren(): List<TemplateContext> = children.values.toList()

    override fun onApplyRule(contextPath: String, vararg rules: TemplateRule) {
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

    override fun onAddChild(layer: Layer): TemplateContext {
        val id = layer.id
        if (children.containsKey(id)) {
            throw IllegalStateException("multiple children found with given id $id")
        }
        val child = DefaultTemplateContext(
            layerPath = layerPath.resolve(layer.name),
            registry = registry,
            layer = layer
        )
        children[id] = child
        return child
    }
}