package kotli.engine.provider.vcs

import kotli.engine.BaseFeatureProvider
import kotli.engine.FeatureProcessor
import kotli.engine.FeatureType
import kotli.engine.model.FeatureTypes
import kotli.engine.provider.vcs.git.GitProcessor

internal object VcsProvider : BaseFeatureProvider() {

    override fun getId(): String = "vcs"
    override fun getType(): FeatureType = FeatureTypes.Metadata

    override fun createProcessors(): List<FeatureProcessor> = listOf(
        GitProcessor
    )

}