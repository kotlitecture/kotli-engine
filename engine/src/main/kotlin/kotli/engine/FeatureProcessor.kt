package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Feature processor is responsible for inclusion or exclusion of the feature it implements
 * in the generated template.
 *
 * Feature is any atomic integrations, technical solutions or business flows
 * which can be added to a layer during its configuration in Kotli.
 *
 * Each feature should be self-describable so it can be presented to the user with icon, title, description, links
 * and any other metadata required to understand its value and purpose.
 *
 * The main advantage of a feature is to provide ready-to-use solution with a minimum configuration required
 * (zero configuration is a goal).
 *
 * By default, each template is pre-configured to include all features.
 */
interface FeatureProcessor : DependencyProvider<FeatureProcessor> {

    /**
     * Unique identifier of the feature.
     */
    fun getId(): String

    /**
     * Icon of the feature. Preferred format is SVG or any other image format of size at least 64px.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return URL on the image resource (default value is icon.svg relative to the package declaration).
     */
    fun getIcon(context: TemplateContext = TemplateContext.Empty): URL? = ResourceUtils.get(this, "icon.svg")

    /**
     * Title of the feature. Short name. Used to display to the user and generate required documentation.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return text (default value is value of title.md, relative to the package declaration).
     */
    fun getTitle(context: TemplateContext = TemplateContext.Empty): String? = ResourceUtils.getAsString(this, "title.md")

    /**
     * Description of the feature. Detailed. Used to display to the user and generate required documentation.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return text (default value is value of description.md, relative to the package declaration).
     */
    fun getDescription(context: TemplateContext = TemplateContext.Empty): String? = ResourceUtils.getAsString(this, "description.md")

    /**
     * Instruction how to use the feature in generated template properly.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return text (default value is value of usage.md, relative to the package declaration).
     */
    fun getUsage(context: TemplateContext = TemplateContext.Empty): String? = ResourceUtils.getAsString(this, "usage.md")

    /**
     * Step-by-step instruction how to configure the feature in generated template so it can be used properly.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return text (default value is value of configuration.md, relative to the package declaration).
     */
    fun getConfiguration(context: TemplateContext = TemplateContext.Empty): String? = ResourceUtils.getAsString(this, "configuration.md")

    /**
     * Approximate time estimate, required to configure the given feature by following available step-by-step instructions.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return value in milliseconds.
     */
    fun getConfigurationEstimate(context: TemplateContext = TemplateContext.Empty): Long = 0L

    /**
     * Approximate time estimate, required to integrate the given feature from scratch
     * (kind of, how many time it takes to include the functionality into the app, as if it was done without template]).
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return value in milliseconds.
     */
    fun getIntegrationEstimate(context: TemplateContext = TemplateContext.Empty): Long = 0L

    /**
     * Approximate impact on the final size of the artifact (apk, jar, etc) when this feature is included.
     * (the size must be measured in bytes and only as if artifact is built in release/production mode).
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return value in bytes.
     */
    fun getSizeImpact(context: TemplateContext = TemplateContext.Empty): Long = 0L

    /**
     * If feature has an official site, it should be provided for possibility to overview it by user.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return web url or null.
     */
    fun getWebUrl(context: TemplateContext = TemplateContext.Empty): String? = null

    /**
     * If feature has an official integration instruction, it should be provided for possibility to overview it by user.
     *
     * @param context is current runtime template context with user defined parameters.
     *
     * @return web url or null.
     */
    fun getIntegrationUrl(context: TemplateContext = TemplateContext.Empty): String? = null

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