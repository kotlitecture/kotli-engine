package kotli.engine

/**
 * Feature providers are used to group different implementations of the same functionality
 * by different vendors. Examples
 *
 * 1) your project requires to use analytics events, and you want to log such events into one
 *    or multiple different services (Google Analytics, Amplitude, AppsFlyer).
 * 2) your project requires to be published in different distribution channels
 *    (Maven, Google Artifact Registry, AWS CodeArtifact, etc).
 *
 * In both scenarios we can use either one or multiple services (processors). And based on the scenario,
 * you will get all required technical solutions to either log events into two systems using one common method
 * or deploy artifacts into one or multiple systems using one generated pipeline.
 *
 * The main idea is to group multiple similar services, present them to user and generate all required artifacts
 * making it possible to operate with the services as with one.
 */
interface IFeatureProvider : IDictionary {

    /**
     * Logical type of the feature this provider is responsible for.
     */
    fun getType(): IFeatureType

    /**
     * If true - it is technically possible to use multiple processors of this feature.
     * If false - only one processor can be applied to the template.
     */
    fun isMultiple(): Boolean = true

    /**
     * Returns all dependencies of given provider.
     * Dependencies must be known at runtime through providers registered in generator.
     */
    fun dependencies(): List<Class<out IFeatureProcessor>> = emptyList()

    /**
     * Returns all available processors of the feature.
     */
    fun getProcessors(): List<IFeatureProcessor>

    /**
     * Returns processor by its id.
     */
    fun getProcessor(id: String): IFeatureProcessor

}