package kotli.engine.provider.configuration.markdown

import kotli.engine.BaseFeatureProcessor
import kotli.engine.FeatureProcessor
import kotli.engine.FeatureProvider
import kotli.engine.TemplateState
import kotli.engine.template.rule.WriteText

internal class MarkdownConfigurationProcessor : BaseFeatureProcessor() {

    override fun getId(): String = "configuration.markdown_configuration"

    override fun isInternal(): Boolean = true

    override fun doApply(state: TemplateState) {
        val templateProcessor = state.processor
        state.layer.features
            .asSequence()
            .map { it.id }
            .mapNotNull(templateProcessor::getFeatureProcessor)
            .sortedBy { templateProcessor.getFeatureProcessorOrder(it.getId()) }
            .map { getDependencies(state, it) }
            .flatten()
            .distinct()
            .toList()
            .filter { it.getConfiguration(state) != null }
            .groupBy { templateProcessor.getFeatureProvider(it::class.java) }
            .filterKeys { it != null }
            .onEachIndexed { i, group -> proceedInstruction(i, state, group.key!!, group.value) }
    }

    private fun proceedInstruction(
        index: Int,
        state: TemplateState,
        provider: FeatureProvider,
        processors: List<FeatureProcessor>
    ) {
        logger.debug("proceedInstruction for provider:\n\t{}", provider.getId())
        val textBuilder = StringBuilder()
        processors.forEach { processor ->
            processor.getTitle(state)?.let { title ->
                val prefix = provider.getTitle()?.takeIf { it != title }
                textBuilder.appendLine()
                if (prefix == null) {
                    textBuilder.appendLine("# $title")
                } else {
                    textBuilder.appendLine("# $prefix :: $title")
                }
            }
            processor.getDescription(state)?.let { description ->
                textBuilder.appendLine()
                textBuilder.appendLine("```")
                textBuilder.appendLine(description)
                textBuilder.appendLine("```")
            }
            val webUrl = processor.getWebUrl(state)
            val integrationUrl = processor.getIntegrationUrl(state)
            if (webUrl != null || integrationUrl != null) {
                textBuilder.appendLine()
                textBuilder.appendLine("## Links")
                textBuilder.appendLine()
                webUrl?.let { textBuilder.appendLine("[Official site](${it})") }
                integrationUrl?.let { textBuilder.appendLine("[Integration instruction](${it})") }
            }
            processor.getConfiguration(state)?.let { instruction ->
                textBuilder.appendLine()
                textBuilder.appendLine("## Configuration")
                textBuilder.appendLine()
                textBuilder.appendLine(instruction)
            }
        }
        state.onApplyRules(
            "docs/integrations/${index + 1} - ${provider.getId()}.md",
            WriteText(textBuilder.trim().toString())
        )
    }
}