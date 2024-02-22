package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Each feature is self-describable so it can be presented to the user with icon, title, description, links
 * and any other metadata required to understand its value and purpose.
 *
 * FeatureDescriptor is responsible to provide all such metadata.
 */
interface FeatureDescriptor {

    /**
     * Unique identifier of the feature.
     */
    fun getId(): String

    /**
     * Icon of the feature. Preferred format is SVG or any other image format of size at least 64px.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return URL on the image resource (default value is icon.svg relative to the package declaration).
     */
    fun getIcon(state: TemplateState = TemplateState.Empty): URL? = ResourceUtils.get(this, "icon.svg")

    /**
     * Title of the feature. Short name. Used to display to the user and generate required documentation.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return text (default value is value of title.md, relative to the package declaration).
     */
    fun getTitle(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "title.md")

    /**
     * Description of the feature. Detailed. Used to display to the user and generate required documentation.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return text (default value is value of description.md, relative to the package declaration).
     */
    fun getDescription(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "description.md")

    /**
     * Instruction how to use the feature in generated template properly.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return text (default value is value of usage.md, relative to the package declaration).
     */
    fun getUsage(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "usage.md")

    /**
     * Step-by-step instruction how to configure the feature in generated template so it can be used properly.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return text (default value is value of configuration.md, relative to the package declaration).
     */
    fun getConfiguration(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "configuration.md")

    /**
     * Approximate time estimate, required to configure the given feature by following available step-by-step instructions.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return value in milliseconds.
     */
    fun getConfigurationEstimate(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * Approximate time estimate, required to integrate the given feature from scratch
     * (kind of, how many time it takes to include the functionality into the app, as if it was done without template]).
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return value in milliseconds.
     */
    fun getIntegrationEstimate(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * Approximate impact on the final size of the artifact (apk, jar, etc) when this feature is included.
     * (the size must be measured in bytes and only as if artifact is built in release/production mode).
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return value in bytes.
     */
    fun getSizeImpact(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * If feature has an official site, it should be provided for possibility to overview it by user.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return web url or null.
     */
    fun getWebUrl(state: TemplateState = TemplateState.Empty): String? = null

    /**
     * If feature has an official integration instruction, it should be provided for possibility to overview it by user.
     *
     * @param state is current runtime template state with user defined parameters.
     *
     * @return web url or null.
     */
    fun getIntegrationUrl(state: TemplateState = TemplateState.Empty): String? = null

}