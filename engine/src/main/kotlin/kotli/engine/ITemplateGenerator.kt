package kotli.engine

import kotli.engine.model.LayerType

interface ITemplateGenerator : IDictionary {

    /**
     * Type of the layer this generator is responsible for.
     */
    val type: ILayerType

    /**
     * Returns URL on a public repository with source codes of the given template.
     */
    fun getRepositoryUrl(): String? = null

    /**
     * Returns current version of the generator.
     * This version is updated automatically each time the new artifact is released.
     */
    fun getVersion(): String = javaClass.`package`.implementationVersion ?: "0.0.0"

    /**
     * Finds a processor by its type.
     *
     * @throws IllegalStateException if a processor with given type is not associated with the generator.
     */
    fun getProcessor(type: Class<out IFeatureProcessor>): IFeatureProcessor

    /**
     * Finds a provider by its type.
     *
     * @throws IllegalStateException if a provider with given type is not associated with the generator.
     */
    fun getProvider(type: Class<out IFeatureProcessor>): IFeatureProvider

    /**
     * Returns all registered providers of the generator.
     */
    fun getProviders(): List<IFeatureProvider>

    /**
     * Generates new template based on the context provided.
     */
    fun generate(context: TemplateContext)

    companion object {
        val App = object : AbstractTemplateGenerator() {
            override fun doRegister() = Unit
            override fun getId(): String = "app"
            override val type: ILayerType = LayerType.App
            override fun doPrepare(context: TemplateContext) = Unit
            override fun createProviders(): List<IFeatureProvider> = emptyList()
        }
    }

}