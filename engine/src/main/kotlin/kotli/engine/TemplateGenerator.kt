package kotli.engine

import kotli.engine.model.LayerTypes

/**
 * Template generator is responsible for fulfillment of the template context with data
 * required to get output structure. But it does not produce such structure,
 * only prepares the data required.
 */
interface TemplateGenerator : TemplateDescriptor {

    /**
     * Prepares the given context with all metadata required for further generation of output structure.
     */
    suspend fun prepare(context: TemplateContext)

    companion object {
        val App = object : BaseTemplateGenerator() {
            override fun getId(): String = "app"
            override fun getType(): LayerType = LayerTypes.App
            override fun doPrepare(state: TemplateState) = Unit
            override fun createProviders(): List<FeatureProvider> = emptyList()
        }
    }

}