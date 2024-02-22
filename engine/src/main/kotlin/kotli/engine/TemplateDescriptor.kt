package kotli.engine

/**
 * Provides all the required metadata to describe this processor.
 */
interface TemplateDescriptor : Dictionary, DependencyProvider<FeatureProcessor> {

    /**
     * Type of the layer this processor is responsible for.
     */
    fun getType(): LayerType

    /**
     * Returns the path to the template directory.
     *
     * Default implementation is configured to take the template structure relative
     * to the resources directory of the jar artifact storing the processor logic.
     */
    fun getTemplatePath(): String = "kotli/templates/${getId()}"

    /**
     * Returns a URL on a public repository with source codes or the official site of the given template.
     */
    fun getWebUrl(): String? = null

    /**
     * Returns the current version of the processor.
     * This version is updated automatically each time the new artifact is released.
     */
    fun getVersion(): String = javaClass.`package`.implementationVersion ?: "0.0.0"

    /**
     * Finds a processor by its id.
     *
     * @throws IllegalStateException if a processor with the given id is not found.
     */
    fun getProcessor(id: String): FeatureProcessor

    /**
     * Finds a processor by its type.
     *
     * @throws IllegalStateException if a processor with the given type is not found.
     */
    fun getProcessor(type: Class<out FeatureProcessor>): FeatureProcessor

    /**
     * Finds a provider by its type.
     *
     * @throws IllegalStateException if a provider with the given type is not found.
     */
    fun getProvider(type: Class<out FeatureProcessor>): FeatureProvider

    /**
     * Returns all registered providers.
     */
    fun getProviders(): List<FeatureProvider>

}