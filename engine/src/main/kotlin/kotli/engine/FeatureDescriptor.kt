package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Each feature is self-describable, providing metadata such as an icon, title, description, links, and other details
 * required to understand its value and purpose. The FeatureDescriptor is responsible for providing this metadata.
 */
interface FeatureDescriptor {

    /**
     * Gets the unique identifier of the feature.
     */
    fun getId(): String

    /**
     * Gets the icon of the feature. Preferred format is SVG or any other image format of size at least 64px.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return URL to the image resource (default value is "icon.svg" relative to the package declaration).
     */
    fun getIcon(state: TemplateState = TemplateState.Empty): URL? = ResourceUtils.get(this, "icon.svg")

    /**
     * Gets the title of the feature. Short name. Used to display to the user and generate required documentation.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Text (default value is the content of "title.md", relative to the package declaration).
     */
    fun getTitle(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "title.md")

    /**
     * Gets the description of the feature. Detailed. Used to display to the user and generate required documentation.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Text (default value is the content of "description.md", relative to the package declaration).
     */
    fun getDescription(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "description.md")

    /**
     * Gets the instruction on how to use the feature in the generated template properly.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Text (default value is the content of "usage.md", relative to the package declaration).
     */
    fun getUsage(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "usage.md")

    /**
     * Gets the step-by-step instruction on how to configure the feature in the generated template so it can be used properly.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Text (default value is the content of "configuration.md", relative to the package declaration).
     */
    fun getConfiguration(state: TemplateState = TemplateState.Empty): String? = ResourceUtils.getAsString(this, "configuration.md")

    /**
     * Gets the approximate time estimate required to configure the given feature by following available step-by-step instructions.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Value in milliseconds.
     */
    fun getConfigurationEstimate(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * Gets the approximate time estimate required to integrate the given feature from scratch
     * (kind of, how much time it takes to include the functionality into the app, as if it was done without the template).
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Value in milliseconds.
     */
    fun getIntegrationEstimate(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * Gets the approximate impact on the final size of the artifact (APK, JAR, etc.) when this feature is included.
     * (The size must be measured in bytes and only as if the artifact is built in release/production mode).
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Value in bytes.
     */
    fun getSizeImpact(state: TemplateState = TemplateState.Empty): Long = 0L

    /**
     * If the feature has an official site, it should be provided for the possibility to overview it by the user.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Web URL or null.
     */
    fun getWebUrl(state: TemplateState = TemplateState.Empty): String? = null

    /**
     * If the feature has an official integration instruction, it should be provided for the possibility to overview it by the user.
     *
     * @param state Current runtime template state with user-defined parameters.
     *
     * @return Web URL or null.
     */
    fun getIntegrationUrl(state: TemplateState = TemplateState.Empty): String? = null

}