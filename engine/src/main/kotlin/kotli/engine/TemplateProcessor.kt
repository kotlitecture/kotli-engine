package kotli.engine

import kotli.engine.model.Layer
import kotli.engine.model.LayerTypes

/**
 * The template processor is responsible for fulfilling the template context with data required
 * to obtain the output structure. It does not produce the output structure itself; instead,
 * it only prepares the data required for further usage by the [kotli.engine.TemplateGenerator].
 */
interface TemplateProcessor : TemplateDescriptor {

    /**
     * Processes the given context with all metadata required for further generation of output structure.
     *
     * @param context The template context to be prepared.
     */
    suspend fun process(context: TemplateContext)

    /**
     * Gets presets for the given template context. Presets contain features that are predefined or suggested for
     * the template. These presets can help users quickly configure their project based on common use cases
     * or recommended configurations.
     *
     * @param context The template context for which presets are to be created.
     * @return A list of features representing the presets for the template context.
     */
    fun getPresets(context: TemplateContext): List<Layer> = emptyList()

    companion object {
        val App = object : BaseTemplateProcessor() {
            override fun getId(): String = "app"
            override fun getType(): LayerType = LayerTypes.App
            override fun createProviders(): List<FeatureProvider> = emptyList()
        }
    }

}