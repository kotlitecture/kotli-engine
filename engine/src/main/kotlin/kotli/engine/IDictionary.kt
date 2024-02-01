package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

/**
 * Just common attributes describing the instances of some object.
 */
interface IDictionary {

    /**
     * Unique identifier of an instance to distinguish it among the others.
     */
    fun getId(): String

    /**
     * Icon of an instance. Preferred format is SVG or any other image format of size at least 64px.
     *
     * @return URL on the image resource (default value is icon.svg relative to the package declaration).
     */
    fun getIcon(): URL? = ResourceUtils.get(this, "icon.svg")

    /**
     * Title of an instance. Short name. Used to display to the user and generate required documentation.
     *
     * @return text (default value is value of title.md, relative to the package declaration).
     */
    fun getTitle(): String? = ResourceUtils.getAsString(this, "title.md")

    /**
     * Description of an instance. Detailed. Used to display to the user and generate required documentation.
     *
     * @return text (default value is value of description.md, relative to the package declaration).
     */
    fun getDescription(): String? = ResourceUtils.getAsString(this, "description.md")

}