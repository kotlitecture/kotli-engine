package kotli.engine.provider.readme

import kotli.engine.BaseFeatureProvider
import kotli.engine.FeatureProcessor
import kotli.engine.FeatureType
import kotli.engine.model.FeatureTypes
import kotli.engine.provider.readme.markdown.MarkdownReadmeProcessor

internal object ReadmeProvider : BaseFeatureProvider() {

    override fun getId(): String = "readme"
    override fun getType(): FeatureType = FeatureTypes.Documentation

    override fun createProcessors(): List<FeatureProcessor> = listOf(
        MarkdownReadmeProcessor
    )

}