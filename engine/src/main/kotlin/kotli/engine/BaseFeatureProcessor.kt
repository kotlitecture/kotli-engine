package kotli.engine

import kotli.engine.model.Feature
import org.slf4j.LoggerFactory

/**
 * Basic implementation of any processor created.
 */
abstract class BaseFeatureProcessor : FeatureProcessor {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    final override fun apply(context: TemplateContext) {
        val id = getId()
        val generator = context.generator
        generator.getProvider(this::class.java).dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        if (!context.isApplied(id)) {
            try {
                val feature = context.getFeature(id) ?: Feature(id)
                logger.debug("apply :: {}", id)
                context.applied[id] = feature
                doApply(context, feature)
            } catch (e: Exception) {
                logger.error("apply :: $id", e)
                context.applied.remove(id)
                throw e
            }
        }
    }

    override fun remove(context: TemplateContext) {
        val id = getId()
        if (!context.removed.containsKey(id)) {
            try {
                val feature = context.getFeature(id) ?: Feature(id)
                logger.debug("remove :: {}", id)
                context.removed[id] = feature
                doRemove(context, feature)
            } catch (e: Exception) {
                logger.error("remove :: $id", e)
                context.removed.remove(id)
                throw e
            }
        }
    }

    protected open fun doApply(context: TemplateContext, feature: Feature) = doApply(context)
    protected open fun doRemove(context: TemplateContext, feature: Feature) = doRemove(context)
    protected open fun doApply(context: TemplateContext) = Unit
    protected open fun doRemove(context: TemplateContext) = Unit

}