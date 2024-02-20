package kotli.engine.provider.configuration.markdown

import kotli.engine.BaseFeatureProcessor
import kotli.engine.FeatureProcessor
import kotli.engine.FeatureProvider
import kotli.engine.TemplateContext
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

internal class MarkdownConfigurationProcessor : BaseFeatureProcessor() {

    override fun getId(): String = "configuration.markdown_configuration"

    override fun doApply(context: TemplateContext) {
        val generator = context.generator
        val processors = mutableSetOf<FeatureProcessor>()
        context.layer.features
            .map { it.id }
            .map(generator::getProcessor)
            .onEach { processor ->
                generator.getProvider(this::class.java).dependencies()
                    .map(generator::getProcessor)
                    .onEach(processors::add)
                dependencies()
                    .map(generator::getProcessor)
                    .onEach(processors::add)
                processors.add(processor)
            }
        logger.debug("found processors :: {}", processors.size)
        processors
            .filter { it !== this }
            .filter { it.getConfiguration(context) != null }
            .groupBy { generator.getProvider(it::class.java) }
            .onEachIndexed { i, group -> proceedInstruction(i, context, group.key, group.value) }
    }

    private fun proceedInstruction(
        index: Int,
        context: TemplateContext,
        provider: FeatureProvider,
        processors: List<FeatureProcessor>
    ) {
        logger.debug("proceedInstruction for provider:\n\t{}", provider.getId())
        val instruction = context.target.resolve("docs/integrations/${index + 1} - ${provider.getId()}.md")
        instruction.parent.createDirectories()
        val textBuilder = StringBuilder()
        processors.forEach { processor ->
            processor.getTitle(context)?.let { title ->
                val prefix = provider.getTitle()?.takeIf { it != title }
                textBuilder.appendLine()
                if (prefix == null) {
                    textBuilder.appendLine("# $title")
                } else {
                    textBuilder.appendLine("# $prefix :: $title")
                }
            }
            processor.getDescription(context)?.let { description ->
                textBuilder.appendLine()
                textBuilder.appendLine("```")
                textBuilder.appendLine(description)
                textBuilder.appendLine("```")
            }
            val webUrl = processor.getWebUrl(context)
            val integrationUrl = processor.getIntegrationUrl(context)
            if (webUrl != null || integrationUrl != null) {
                textBuilder.appendLine()
                textBuilder.appendLine("## Links")
                textBuilder.appendLine()
                webUrl?.let { textBuilder.appendLine("[Official site](${it})") }
                integrationUrl?.let { textBuilder.appendLine("[Integration instruction](${it})") }
            }
            processor.getConfiguration(context)?.let { instruction ->
                textBuilder.appendLine()
                textBuilder.appendLine("## Configuration")
                textBuilder.appendLine()
                textBuilder.appendLine(instruction)
            }
        }
        val text = textBuilder.trim().toString()
        instruction.writeText(text)
    }
}