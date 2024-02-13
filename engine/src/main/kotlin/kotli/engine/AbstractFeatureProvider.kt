package kotli.engine

import org.slf4j.LoggerFactory

/**
 * Basic implementation of any provider created.
 */
abstract class AbstractFeatureProvider : IFeatureProvider {

    private val all by lazy { createProcessors() }
    private val byId by lazy { all.associateBy { it.getId() } }

    override fun getProcessors(): List<IFeatureProcessor> = all
    override fun getProcessor(id: String): IFeatureProcessor = byId[id] ?: UnknownProcessor(id)

    /**
     * Should return all available processors of the given feature provider.
     * Processor instances must be stateless.
     */
    abstract fun createProcessors(): List<IFeatureProcessor>

    class UnknownProcessor(private val id: String) : IFeatureProcessor {

        private val logger = LoggerFactory.getLogger(UnknownProcessor::class.java)

        override fun getId(): String = id

        override fun apply(context: TemplateContext) {
            logger.debug("apply unknown processor :: {}", id)
        }

        override fun remove(context: TemplateContext) {
            logger.debug("remove unknown processor :: {}", id)
        }
    }

}