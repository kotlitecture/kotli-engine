package kotli.engine.provider.configuration

import kotli.engine.BaseFeatureProvider
import kotli.engine.FeatureProcessor
import kotli.engine.FeatureType
import kotli.engine.model.FeatureTypes
import kotli.engine.provider.configuration.markdown.MarkdownConfigurationProcessor

internal class ConfigurationProvider : BaseFeatureProvider() {

    override fun getId(): String = "configuration"
    override fun getType(): FeatureType = FeatureTypes.Documentation

    override fun createProcessors(): List<FeatureProcessor> = listOf(
        MarkdownConfigurationProcessor()
    )

}