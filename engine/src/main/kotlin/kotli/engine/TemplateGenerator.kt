package kotli.engine

import kotli.engine.model.LayerTypes

/**
 * The template generator is responsible for fulfilling the template context with data required
 * to obtain the output structure. It does not produce the output structure itself; instead,
 * it only prepares the data required for further usage by the [kotli.flow.TemplateFlow].
 */
interface TemplateGenerator : TemplateDescriptor {

    /**
     * Prepares the given context with all metadata required for further generation of output structure.
     *
     * @param context The template context to be prepared.
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