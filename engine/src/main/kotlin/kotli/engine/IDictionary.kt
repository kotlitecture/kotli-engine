package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL

interface IDictionary {

    fun getId(): String

    fun getVersion(): String = javaClass.`package`.implementationVersion ?: "0.0.0"

    fun getIcon(): URL? = ResourceUtils.get(this, "icon.svg")

    fun getTitle(): String? = ResourceUtils.getAsString(this, "title.md")

    fun getDescription(): String? = ResourceUtils.getAsString(this, "description.md")

}