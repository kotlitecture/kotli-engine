package kotli.engine

/**
 * Basic implementation of any provider created.
 */
abstract class BaseFeatureProvider : FeatureProvider {

    private val all by lazy { createProcessors() }

    override fun getProcessors(): List<FeatureProcessor> = all

    /**
     * Should return all available processors of the given feature provider.
     * Processor instances must be stateless.
     */
    abstract fun createProcessors(): List<FeatureProcessor>

}