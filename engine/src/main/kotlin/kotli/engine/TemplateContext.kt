package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateRule

/**
 * Execution context for template.
 */
interface TemplateContext : TemplateState {

    /**
     * Applies feature if it is not applied yet.
     *
     * The implementation is responsible for checking if feature is already applied or not
     * to keep the context in consistent taking into account that features can be applied in parallel.
     */
    fun onApplyFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Removes feature if it is not removed yet.
     *
     * The implementation is responsible for checking if feature is already deleted or not
     * to keep the context in consistent taking into account that features can be removed in parallel.
     */
    fun onRemoveFeature(id: String, action: (feature: Feature) -> Unit)

    /**
     * Adds new child context.
     */
    fun onAddChild(layer: Layer): TemplateContext?

}