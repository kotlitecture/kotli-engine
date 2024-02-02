package kotli.engine

import kotli.engine.model.FeatureType
import kotli.engine.utils.PathUtils
import org.slf4j.LoggerFactory
import java.nio.file.Files
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

/**
 * Basic implementation of any template generator.
 */
abstract class AbstractTemplateGenerator : ITemplateGenerator {

    init {
        register()
    }

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }
    protected abstract fun doPrepare(context: TemplateContext)
    protected abstract fun createProviders(): List<IFeatureProvider>

    private val templatePath: String by lazy { "kotli/templates/${getId()}" }
    private val providerById by lazy { providerList.associateBy { it.id } }
    private val providerList by lazy { createProviders() }

    private val processorByType by lazy {
        providerList
            .map { it.getProcessors() }
            .flatten()
            .associateBy { it::class.java }
    }

    private val providersByProcessorType by lazy {
        providerList.map { provider -> provider.getProcessors().map { it::class.java to provider } }
            .flatten()
            .toMap()
    }

    override fun getProviders(): List<IFeatureProvider> {
        return providerList.filter { it.type != FeatureType.Transitive }
    }

    override fun getProcessor(type: Class<out IFeatureProcessor>): IFeatureProcessor {
        return processorByType[type] ?: throw IllegalStateException("no processor :: $type")
    }

    override fun getProvider(type: Class<out IFeatureProcessor>): IFeatureProvider {
        return providersByProcessorType[type] ?: throw IllegalStateException("no provider :: $type")
    }

    override fun generate(context: TemplateContext) {
        prepare(context)
        proceedChildren(context)
        applyProcessors(context)
        removeProcessors(context)
        proceedInstructions(context)
        cleanup(context)
    }

    protected open fun doRegister() {
        TemplateFactory.register(this)
    }

    private fun register() {
        doRegister()
    }

    private fun prepare(context: TemplateContext) {
        val from = PathUtils.getFromResource(templatePath) ?: return
        val to = context.target
        PathUtils.copy(from, to)
        doPrepare(context)
    }

    private fun cleanup(context: TemplateContext) {
        Files.walk(context.target)
            .filter(PathUtils::isEmptyDir)
            .forEach(PathUtils::delete)
    }

    private fun proceedChildren(context: TemplateContext) {
        context.layer.layers
            .map { childLayer ->
                Kotli(
                    target = context.target.resolve(childLayer.name),
                    layer = childLayer
                )
            }
            .forEach { it.generate() }
    }

    private fun applyProcessors(context: TemplateContext) {
        context.layer.features.forEach { feature ->
            val provider = getProvider(feature.providerId)
            val processor = provider.getProcessor(feature.processorId)
            processor.apply(context)
        }
    }

    private fun removeProcessors(context: TemplateContext) {
        providerList.forEach { provider ->
            provider.getProcessors()
                .filter { processor -> !context.applied.contains(processor) }
                .forEach { processor -> processor.remove(context) }
        }
    }

    private fun proceedInstructions(context: TemplateContext) {
        context.applied
            .filter { it.getConfiguration(context) != null }
            .groupBy { getProvider(it::class.java) }
            .onEachIndexed { index, group ->
                val provider = group.key
                val processors = group.value
                proceedInstruction(index, context, provider, processors)
            }
    }

    private fun proceedInstruction(
        index: Int,
        context: TemplateContext,
        provider: IFeatureProvider,
        processors: List<IFeatureProcessor>
    ) {
        logger.debug("proceedInstruction for provider:\n\t{}", provider.id)
        val instruction = context.target.resolve("docs/integrations/${index + 1} - ${provider.id}.md")
        instruction.parent.createDirectories()
        val textBuilder = StringBuilder()
        processors.forEach { processor ->
            processor.getTitle(context)?.let { title ->
                val prefix = provider.getTitle(context)?.takeIf { it != title }
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

    private fun getProvider(id: String): IFeatureProvider {
        return providerById[id] ?: throw IllegalStateException("provider not found :: $id")
    }

}