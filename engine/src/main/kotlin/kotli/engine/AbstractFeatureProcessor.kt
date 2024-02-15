package kotli.engine

import org.slf4j.LoggerFactory

/**
 * Basic implementation of any processor created.
 */
abstract class AbstractFeatureProcessor : IFeatureProcessor {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    final override fun apply(context: TemplateContext) {
        val generator = context.layer.generator
        generator.getProvider(this::class.java).dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        dependencies()
            .map(generator::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
        context.apply(this) {
            logger.debug("apply :: {} -> {}", context.layer.name, javaClass.simpleName)
            doApply(context)
        }
    }

    override fun remove(context: TemplateContext) {
        logger.debug("remove :: {} -> {}", context.layer.name, javaClass.simpleName)
        doRemove(context)
    }

    protected open fun doApply(context: TemplateContext) = Unit
    protected open fun doRemove(context: TemplateContext) = Unit

}