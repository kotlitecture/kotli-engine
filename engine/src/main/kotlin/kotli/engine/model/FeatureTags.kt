package kotli.engine.model

import kotli.engine.FeatureTag

/**
 * Represents a predefined set of feature tags.
 */
enum class FeatureTags(
    private val title: String,
    private val color: String
) : FeatureTag {

    Android("Android", "#8BD294"),
    IOS("iOS", "#BDBDBD"),
    Web("Web", "#E9D575"),
    Desktop("Desktop", "#C05048"),
    Mobile("Mobile", "#5B77C0"),
    Server("Server", "#3B476D"),
    Client("Client", "#6457D8"),
    ;

    override fun getTitle(): String = title
    override fun getColor(): String = color
}