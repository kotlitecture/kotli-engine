package kotli.engine

/**
 * Feature providers are used to group different implementations of the same functionality
 * by different vendors. Examples:
 *
 * 1) Your project requires the use of analytics events, and you want to log such events into one
 *    or multiple different services (Google Analytics, Amplitude, AppsFlyer).
 * 2) Your project requires publication in different distribution channels
 *    (Maven, Google Artifact Registry, AWS CodeArtifact, etc.).
 *
 * In both scenarios, you can use either one or multiple services (processors). Depending on the scenario,
 * you will get all the required technical solutions to either log events into two systems using one common method
 * or deploy artifacts into one or multiple systems using one generated pipeline.
 *
 * The main idea is to group multiple similar services, present them to the user, and generate all required artifacts,
 * making it possible to operate with the services as one.
 */
interface FeatureProvider : Dictionary, DependencyProvider<FeatureProcessor> {

    /**
     * Represents the logical type of the feature that this provider is responsible for.
     */
    fun getType(): FeatureType

    /**
     * Returns true if it is technically possible to use multiple processors of this feature,
     * or false if only one processor can be applied to the template.
     */
    fun isMultiple(): Boolean = true

    /**
     * Retrieves a list of all available processors for the feature.
     */
    fun getProcessors(): List<FeatureProcessor>

}