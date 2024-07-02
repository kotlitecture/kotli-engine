package kotli.engine

/**
 * `FeatureTag` represents a specific attribute of a feature.
 *
 * It provides methods to get the title and optionally the color of the feature tag.
 */
interface FeatureTag {

    /**
     * Gets the title of the feature tag.
     *
     * @return the title of the feature tag as a `String`.
     */
    fun getTitle(): String

    /**
     * Gets the color of the feature tag.
     * This method provides a default implementation that returns `null`,
     * meaning the color is optional and may not be set.
     *
     * @return the color of the feature tag as a `String`, or `null` if the color is not set.
     */
    fun getColor(): String? = null

}