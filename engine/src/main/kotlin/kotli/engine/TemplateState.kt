package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.FileRule
import kotli.engine.template.FileRules
import java.nio.file.Path

/**
 * Read-only state of execution context during template generation.
 */
interface TemplateState {

    /**
     * The layer associated with this template state.
     */
    val layer: Layer

    /**
     * The path to the layer in the file system.
     */
    val layerPath: Path

    /**
     * The template processor responsible for this state.
     */
    val processor: TemplateProcessor

    /**
     * Returns all available rules to be applied.
     *
     * @return A list of template rules.
     */
    fun getRules(): List<FileRules>

    /**
     * Finds a feature by its ID in the provided layer or returns null if not found.
     *
     * @param id The ID of the feature.
     * @return The feature with the specified ID, or null if not found.
     */
    fun getFeature(id: String): Feature?

    /**
     * Returns all children contexts of the given one.
     *
     * @return A list of child template states.
     */
    fun getChildren(): List<TemplateState>

    /**
     * Adds rules to be applied for the current template.
     *
     * @param rules The rules to be applied.
     */
    fun onApplyRules(rules: FileRules)

    /**
     * Adds rules to be applied for a given file context path relative to the layer path.
     *
     * @param contextPath The relative path to the file context.
     * @param rules The rules to be applied.
     */
    fun onApplyRules(contextPath: String, vararg rules: FileRule) {
        onApplyRules(FileRules(contextPath, rules.toList()))
    }

    companion object {
        /**
         * An empty implementation of [TemplateState] for cases where no state is available.
         */
        val Empty = object : TemplateState {
            override val processor: TemplateProcessor = TemplateProcessor.App
            override fun getChildren(): List<TemplateState> = emptyList()
            override fun getRules(): List<FileRules> = emptyList()
            override fun getFeature(id: String): Feature? = null
            override fun onApplyRules(rules: FileRules) = Unit
            override val layerPath: Path = Path.of("/")
            override val layer: Layer = Layer(
                    id = "<YOUR_LAYER_ID>",
                    name = "<YOUR_LAYER_NAME>",
                    namespace = "<YOUR_LAYER_NAMESPACE>",
                    processorId = TemplateProcessor.App.getId(),
            )
        }
    }
}