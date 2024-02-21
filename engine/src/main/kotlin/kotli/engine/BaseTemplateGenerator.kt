package kotli.engine

import kotli.engine.provider.configuration.ConfigurationProvider
import kotli.engine.provider.configuration.markdown.MarkdownConfigurationProcessor
import kotli.engine.utils.PathUtils
import org.slf4j.LoggerFactory
import java.nio.file.Files

/**
 * Basic implementation of any template generator.
 */
abstract class BaseTemplateGenerator : TemplateGenerator {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    private val templatePath: String by lazy { "kotli/templates/${getId()}" }
    private val providerList by lazy { createProviders().plus(ConfigurationProvider()) }

    override fun dependencies(): List<Class<out FeatureProcessor>> = listOf(
        MarkdownConfigurationProcessor::class.java
    )

    private val processorsByType by lazy {
        providerList
            .map { it.getProcessors() }
            .flatten()
            .associateBy { it::class.java }
    }

    private val processorsById by lazy {
        providerList.map { provider -> provider.getProcessors() }
            .flatten()
            .associateBy { it.getId() }
    }

    private val providersByProcessorType by lazy {
        providerList.map { provider -> provider.getProcessors().map { it::class.java to provider } }
            .flatten()
            .toMap()
    }

    override fun getProviders(): List<FeatureProvider> {
        return providerList
    }

    override fun getProcessor(id: String): FeatureProcessor {
        return processorsById[id] ?: throw IllegalStateException("no processor :: $id")
    }

    override fun getProcessor(type: Class<out FeatureProcessor>): FeatureProcessor {
        return processorsByType[type] ?: throw IllegalStateException("no processor :: $type")
    }

    override fun getProvider(type: Class<out FeatureProcessor>): FeatureProvider {
        return providersByProcessorType[type] ?: throw IllegalStateException("no provider :: $type")
    }

    override fun generate(context: TemplateContext) {
        prepare(context)
        proceedChildren(context)
        applyProcessors(context)
        applyDependencies(context)
        removeProcessors(context)
        cleanup(context)
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
                TemplateContext(
                    target = context.target.resolve(childLayer.name),
                    registry = context.registry,
                    layer = childLayer
                )
            }
            .forEach { it.generator.generate(it) }
    }

    private fun applyProcessors(context: TemplateContext) {
        context.layer.features.forEach { feature ->
            processorsById[feature.id]?.apply(context)
        }
    }

    private fun applyDependencies(context: TemplateContext) {
        dependencies()
            .map(this::getProcessor)
            .onEach { dependency -> dependency.apply(context) }
    }

    private fun removeProcessors(context: TemplateContext) {
        providerList.forEach { provider ->
            provider.getProcessors()
                .filter { processor -> !context.isApplied(processor.getId()) }
                .forEach { processor -> processor.remove(context) }
        }
    }

    protected abstract fun doPrepare(context: TemplateContext)
    protected abstract fun createProviders(): List<FeatureProvider>

}