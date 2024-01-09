package kotli.engine

import kotli.engine.utils.ResourceUtils
import java.net.URL


interface IFeature {

    fun getIcon(context: TemplateContext): URL? = ResourceUtils.get(this, "icon.svg")

    fun getTitle(context: TemplateContext): String? = ResourceUtils.getAsString(this, "title.md")

    fun getLinks(context: TemplateContext): String? = ResourceUtils.getAsString(this, "links.md")

    fun getDescription(context: TemplateContext): String? = ResourceUtils.getAsString(this, "description.md")

    fun getConfiguration(context: TemplateContext): String? = ResourceUtils.getAsString(this, "configuration.md")

    fun getConfigurationEstimate(context: TemplateContext): Long = 0L

    fun getIntegrationEstimate(context: TemplateContext): Long = 0L

    fun getSizeImpact(context: TemplateContext): Long = 0L

}