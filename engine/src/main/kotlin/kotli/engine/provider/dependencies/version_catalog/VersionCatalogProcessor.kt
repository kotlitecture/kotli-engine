package kotli.engine.provider.dependencies.version_catalog

import kotli.engine.BaseFeatureProcessor
import kotli.engine.TemplateState
import kotli.engine.template.VersionCatalogRules
import kotli.engine.template.rule.CleanupMarkedBlock
import kotli.engine.template.rule.CleanupMarkedLine
import kotli.engine.template.rule.RemoveMarkedBlock
import kotli.engine.template.rule.RemoveMarkedLine
import kotlin.time.Duration.Companion.hours

/**
 * Processor for applying/removing version catalog plugin.
 */
class VersionCatalogProcessor : BaseFeatureProcessor() {

    override fun getId(): String = ID
    override fun getWebUrl(state: TemplateState): String = "https://github.com/littlerobots/version-catalog-update-plugin"
    override fun getIntegrationUrl(state: TemplateState): String = "https://github.com/littlerobots/version-catalog-update-plugin?tab=readme-ov-file#getting-started"
    override fun getIntegrationEstimate(state: TemplateState): Long = 1.hours.inWholeMilliseconds

    override fun doApply(state: TemplateState) {
        state.onApplyRules(
            "build.gradle",
            CleanupMarkedLine("{quality.dependencies.versions_catalog}"),
            CleanupMarkedBlock("{quality.dependencies.versions_catalog.config}")
        )
    }

    override fun doRemove(state: TemplateState) {
        state.onApplyRules(
            "build.gradle",
            RemoveMarkedLine("{quality.dependencies.versions_catalog}"),
            RemoveMarkedBlock("{quality.dependencies.versions_catalog.config}")
        )
        state.onApplyRules(
            "gradle.properties",
            RemoveMarkedLine("systemProp.javax.xml")
        )
        state.onApplyRules(
            VersionCatalogRules(
                RemoveMarkedLine("version-catalog")
            )
        )
    }

    companion object {
        const val ID = "quality.dependencies.versions_catalog"
    }

}