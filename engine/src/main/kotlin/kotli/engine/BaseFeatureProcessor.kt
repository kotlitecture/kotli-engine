package kotli.engine

import kotli.engine.model.Feature
import org.slf4j.LoggerFactory

/**
 * Basic implementation of any processor created.
 */
abstract class BaseFeatureProcessor : FeatureProcessor {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    final override fun apply(context: TemplateContext) {
        withDependencies(context, this)
            .minus(this)
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

    protected fun withDependencies(state: TemplateState, processor: FeatureProcessor): List<FeatureProcessor> {
        val generator = state.generator
        val processors = mutableSetOf<FeatureProcessor>()
        generator.getProvider(processor::class.java).dependencies()
            .map(generator::getProcessor)
            .onEach { processors.addAll(withDependencies(state, it)) }
        processor.dependencies()
            .map(generator::getProcessor)
            .onEach { processors.addAll(withDependencies(state, it)) }
        processors.add(processor)
        return processors.toList()
    }

    protected open fun doApply(state: TemplateState, feature: Feature) = doApply(state)
    protected open fun doRemove(state: TemplateState, feature: Feature) = doRemove(state)
    protected open fun doApply(state: TemplateState) = Unit
    protected open fun doRemove(state: TemplateState) = Unit

}