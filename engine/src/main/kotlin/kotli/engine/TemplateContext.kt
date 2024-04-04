package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer

/**
 * Execution context for a template.
 */
interface TemplateContext : TemplateState {

    /**
     * Applies a feature if it is not applied yet.
     *
     * The implementation is responsible for checking if the feature is already applied or not
     * to keep the context consistent, taking into account that features can be applied in parallel.
     */
    fun onApplyFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Removes a feature if it is not removed yet.
     *
     * The implementation is responsible for checking if the feature is already deleted or not
     * to keep the context consistent, taking into account that features can be removed in parallel.
     */
    fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Adds a new child context.
     */
    fun onAddChild(layer: Layer): TemplateContext?

    /**
     * Returns the list of features applied in this context.
     *
     * @return The list of applied features.
     */
    fun getAppliedFeatures(): List<Feature>

    /**
     * Returns the list of features removed in this context.
     *
     * @return The list of removed features.
     */
    fun getRemovedFeatures(): List<Feature>

}