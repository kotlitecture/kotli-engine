package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.LayerType

interface ITemplateGenerator : IDictionary {

    val type: LayerType

    fun getProcessor(type: Class<out IFeatureProcessor>): IFeatureProcessor

    fun getProvider(type: Class<out IFeatureProcessor>): IFeatureProvider

    fun getProviders(): List<IFeatureProvider>

    fun generate(context: TemplateContext)

    fun getAllFeatures(): List<Feature> {
        return getProviders()
            .map { provider ->
                provider.getProcessors()
                    .map { processor ->
                        Feature(
                            providerId = provider.id,
                            processorId = processor.id
                        )
                    }
            }
            .flatten()
    }

    companion object {
        val App = object : AbstractTemplateGenerator() {
            override fun doRegister() = Unit
            override fun getId(): String = ""
            override val templatePath: String = ""
            override val type: LayerType = LayerType.App
            override fun doPrepare(context: TemplateContext) = Unit
            override fun createProviders(): List<IFeatureProvider> = emptyList()
        }
    }

}