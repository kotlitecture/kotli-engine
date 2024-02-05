package kotli.engine

/**
 * Basic implementation of any provider created.
 */
abstract class AbstractFeatureProvider : IFeatureProvider {

    private val all by lazy { createProcessors() }
    private val byId by lazy { all.associateBy { it.id } }

    override val order: Int = -1
    override val multiple: Boolean = true
    override fun getProcessors(): List<IFeatureProcessor> = all
    override fun getProcessor(id: String): IFeatureProcessor = byId[id]!!

    /**
     * Should return all available processors of the given feature provider.
     * Processor instances must be stateless.
     */
    abstract fun createProcessors(): List<IFeatureProcessor>

}