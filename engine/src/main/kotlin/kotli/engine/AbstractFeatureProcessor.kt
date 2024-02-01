package kotli.engine

import org.slf4j.LoggerFactory

/**
 * Basic implementation of any processor created.
 */
abstract class AbstractFeatureProcessor : IFeatureProcessor {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    protected open fun dependencies(): List<Class<out IFeatureProcessor>> = emptyList()

    final override fun apply(context: TemplateContext) {
        val generator = context.layer.generator
        dependencies().forEach { dependency ->
            val processor = generator.getProcessor(dependency)
            processor.apply(context)
        }
        if (context.applied.add(this)) {
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