package kotli.engine

/**
 * Each feature processor is responsible for inclusion or exclusion of the feature it implements
 * in the generated template.
 *
 * By default, each template is pre-configured to include all features.
 */
interface IFeatureProcessor : IFeature {

    /**
     * Applies given processor to the template generated.
     *
     * @param context is current runtime template context with user defined parameters.
     */
    fun apply(context: TemplateContext) = Unit

    /**
     * Removes given processor from the template generated.
     *
     * @param context is current runtime template context with user defined parameters.
     */
    fun remove(context: TemplateContext) = Unit

}