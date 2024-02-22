package kotli.engine

/**
 * Basic implementation of any provider created.
 *
 * This class provides a common base for implementing feature providers and includes
 * a method to retrieve all available processors associated with the provider.
 */
abstract class BaseFeatureProvider : FeatureProvider {

    /**
     * Lazily initialized list of all available processors associated with the provider.
     */
    private val all by lazy { createProcessors() }

    /**
     * Returns a list of all available processors associated with the feature provider.
     *
     * @return A list of feature processors.
     */
    override fun getProcessors(): List<FeatureProcessor> = all

    /**
     * Abstract method to be implemented by subclasses, returning a list of all available processors.
     * Processor instances must be stateless.
     *
     * @return A list of feature processors.
     */
    abstract fun createProcessors(): List<FeatureProcessor>

}