package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Just common attributes describing instances of some object.
 */
interface Dictionary {

    /**
     * Gets the unique identifier of an instance to distinguish it among others.
     *
     * @return The unique identifier.
     */
    fun getId(): String

    /**
     * Gets the order of this dictionary instance.
     *
     * @return The order of the instance.
     */
    fun getOrder(): Int = -1

    /**
     * Gets the icon of an instance. Preferred format is SVG or any other image format of size at least 64px.
     *
     * @return URL to the image resource (default value is "icon.svg" relative to the package declaration).
     */
    fun getIcon(): URL? = ResourceUtils.get(this, "icon.svg")

    /**
     * Gets the title of an instance. Short name. Used to display to the user and generate required documentation.
     *
     * @return Text (default value is the content of "title.md", relative to the package declaration).
     */
    fun getTitle(): String? = ResourceUtils.getAsString(this, "title.md")

    /**
     * Gets the description of an instance. Detailed. Used to display to the user and generate required documentation.
     *
     * @return Text (default value is the content of "description.md", relative to the package declaration).
     */
    fun getDescription(): String? = ResourceUtils.getAsString(this, "description.md")

}