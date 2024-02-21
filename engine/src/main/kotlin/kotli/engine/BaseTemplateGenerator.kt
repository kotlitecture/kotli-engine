package kotli.engine

import kotli.engine.provider.configuration.ConfigurationProvider
import kotli.engine.provider.configuration.markdown.MarkdownConfigurationProcessor
import org.slf4j.LoggerFactory

/**
 * Basic implementation of any template generator.
 */
abstract class BaseTemplateGenerator : TemplateGenerator {

    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

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

    override fun prepare(context: TemplateContext) {
        doPrepare(context)
        proceedChildren(context)
        applyProcessors(context)
        applyDependencies(context)
        removeProcessors(context)
    }

    private fun proceedChildren(context: TemplateContext) {
        context.layer.layers
            .map(context::onAddChild)
            .forEach { it.generator.prepare(it) }
    }

    private fun applyProcessors(context: TemplateContext) {
        context.layer.features.forEach { feature ->
            processorsById[feature.id]?.apply(context)
        }
    }

    private fun applyDependencies(context: TemplateContext) {
        dependencies()
            .map(this::getProcessor)
            .onEach { processor -> processor.apply(context) }
    }

    private fun removeProcessors(context: TemplateContext) {
        providerList.forEach { provider ->
            provider.getProcessors()
                .forEach { processor -> processor.remove(context) }
        }
    }

    protected abstract fun doPrepare(context: TemplateContext)
    protected abstract fun createProviders(): List<FeatureProvider>

}