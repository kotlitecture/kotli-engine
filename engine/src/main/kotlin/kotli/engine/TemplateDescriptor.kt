package kotli.engine

import kotli.engine.model.Layer

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
     */
    fun getFeatureProcessor(id: String): FeatureProcessor?

    /**
     * Finds a processor order by its id.
     */
    fun getFeatureProcessorOrder(id: String): Int

    /**
     * Finds a processor by its type.
     */
    fun getFeatureProcessor(type: Class<out FeatureProcessor>): FeatureProcessor?

    /**
     * Finds a provider by its type.
     *
     * @throws IllegalStateException if a provider with the given type is not found.
     */
    fun getFeatureProvider(type: Class<out FeatureProcessor>): FeatureProvider?

    /**
     * Returns all registered providers.
     */
    fun getFeatureProviders(): List<FeatureProvider>

    /**
     * Gets presets for the given template. Presets contain features that are predefined or suggested for
     * the template. These presets can help users quickly configure their project based on common use cases
     * or recommended configurations.
     *
     * @return A list of features representing the presets for the template context.
     */
    fun getPresets(): List<Layer> = emptyList()

}