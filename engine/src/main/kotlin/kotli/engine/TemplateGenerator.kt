package kotli.engine

import kotli.engine.model.LayerTypes

interface TemplateGenerator : Dictionary, DependencyProvider<FeatureProcessor> {

    /**
     * Type of the layer this generator is responsible for.
     */
    fun getType(): LayerType

    /**
     * Returns path to the template directory.
     *
     * Default implementation is configured to take template structure relative
     * to the resources directory of the jar-artefact storing the generator logic.
     */
    fun getTemplatePath(): String = "kotli/templates/${getId()}"

    /**
     * Returns URL on a public repository with source codes or official site of the given template.
     */
    fun getWebUrl(): String? = null

    /**
     * Returns current version of the generator.
     * This version is updated automatically each time the new artifact is released.
     */
    fun getVersion(): String = javaClass.`package`.implementationVersion ?: "0.0.0"

    /**
     * Finds a processor by its id.
     *
     * @throws IllegalStateException if a processor with given id is not associated with the generator.
     */
    fun getProcessor(id: String): FeatureProcessor

    /**
     * Finds a processor by its type.
     *
     * @throws IllegalStateException if a processor with given type is not associated with the generator.
     */
    fun getProcessor(type: Class<out FeatureProcessor>): FeatureProcessor

    /**
     * Finds a provider by its type.
     *
     * @throws IllegalStateException if a provider with given type is not associated with the generator.
     */
    fun getProvider(type: Class<out FeatureProcessor>): FeatureProvider

    /**
     * Returns all registered providers of the generator.
     */
    fun getProviders(): List<FeatureProvider>

    /**
     * Prepares the given context with all metadata required for further generation of output structure.
     */
    fun prepare(context: TemplateContext)

    companion object {
        val App = object : BaseTemplateGenerator() {
            override fun getId(): String = "app"
            override fun getType(): LayerType = LayerTypes.App
            override fun doPrepare(context: TemplateContext) = Unit
            override fun createProviders(): List<FeatureProvider> = emptyList()
        }
    }

}