package kotli.engine

import kotli.engine.model.Feature
import org.slf4j.LoggerFactory

/**
 * Basic implementation of any processor created.
 */
abstract class BaseFeatureProcessor : FeatureProcessor {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    final override fun apply(context: TemplateContext) {
        val generator = context.generator
        generator.getProvider(this::class.java).dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        context.onApplyFeature(getId()) { feature ->
            doApply(context, feature)
        }
    }

    override fun remove(context: TemplateContext) {
        context.onRemoveFeature(getId()) { feature ->
            doRemove(context, feature)
        }
    }

    protected open fun doApply(context: TemplateContext, feature: Feature) = doApply(context)
    protected open fun doRemove(context: TemplateContext, feature: Feature) = doRemove(context)
    protected open fun doApply(context: TemplateContext) = Unit
    protected open fun doRemove(context: TemplateContext) = Unit

}