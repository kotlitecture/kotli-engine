package kotli.engine

import kotli.engine.provider.configuration.ConfigurationProvider
import kotli.engine.provider.configuration.markdown.MarkdownConfigurationProcessor
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.slf4j.LoggerFactory

/**
 * Basic implementation of any template processor.
 *
 * This class provides a base for implementing template processors and includes methods
 * for managing feature processors, providers, and preparing the template context.
 */
abstract class BaseTemplateProcessor : TemplateProcessor {

    /**
     * Lazily initialized logger for logging within this class.
     */
    protected val logger by lazy { LoggerFactory.getLogger(this::class.java) }

    /**
     * Lazily initialized list of providers associated with the processor.
     * Includes default providers like ConfigurationProvider.
     */
    private val providerList by lazy { createProviders().plus(ConfigurationProvider()) }

    /**
     * Lazily initialized map of processors indexed by their class type.
     */
    private val processorsByType by lazy {
        providerList
                .map { it.getProcessors() }
                .flatten()
                .associateBy { it::class.java }
    }

    /**
     * Lazily initialized map of processors indexed by their unique identifier.
     */
    private val processorsById by lazy {
        providerList.map { provider -> provider.getProcessors() }
                .flatten()
                .associateBy { it.getId() }
    }

    /**
     * Lazily initialized map of providers indexed by the type of processors they provide.
     */
    private val providersByProcessorType by lazy {
        providerList.map { provider -> provider.getProcessors().map { it::class.java to provider } }
                .flatten()
                .toMap()
    }

    override fun dependencies(): List<Class<out FeatureProcessor>> = listOf(
            MarkdownConfigurationProcessor::class.java
    )

    /**
     * Returns a list of all providers associated with the processor.
     *
     * @return A list of feature providers.
     */
    override fun getFeatureProviders(): List<FeatureProvider> {
        return providerList
    }

    /**
     * Retrieves a feature processor by its unique identifier.
     *
     * @param id Unique identifier of the processor.
     * @return The feature processor.
     * @throws IllegalStateException if no processor with the given id is found.
     */
    override fun getFeatureProcessor(id: String): FeatureProcessor {
        return processorsById[id] ?: throw IllegalStateException("no processor :: $id")
    }

    /**
     * Retrieves a feature processor by its class type.
     *
     * @param type Class type of the processor.
     * @return The feature processor.
     * @throws IllegalStateException if no processor with the given type is found.
     */
    override fun getFeatureProcessor(type: Class<out FeatureProcessor>): FeatureProcessor {
        return processorsByType[type] ?: throw IllegalStateException("no processor :: $type")
    }

    /**
     * Retrieves the feature provider associated with the given processor type.
     *
     * @param type Class type of the processor.
     * @return The feature provider.
     * @throws IllegalStateException if no provider is found for the given processor type.
     */
    override fun getFeatureProvider(type: Class<out FeatureProcessor>): FeatureProvider {
        return providersByProcessorType[type] ?: throw IllegalStateException("no provider :: $type")
    }

    /**
     * Prepares the template context by executing the necessary steps, including:
     * 1. Invoking the `doPrepare` method.
     * 2. Processing child layers.
     * 3. Applying feature processors.
     * 4. Applying dependencies.
     * 5. Removing processors.
     *
     * @param context The template context.
     */
    override suspend fun process(context: TemplateContext) {
        prepare(context)
        proceedChildren(context)
        applyProcessors(context)
        applyDependencies(context)
        removeProcessors(context)
    }

    /**
     * Proceeds through child layers, invoking the `prepare` method for each child asynchronously.
     *
     * @param context The template context.
     */
    private suspend fun proceedChildren(context: TemplateContext) {
        coroutineScope {
            context.layer.layers
                    .mapNotNull(context::onAddChild)
                    .map { child -> async { child.processor.process(child) } }
                    .awaitAll()
        }
    }

    /**
     * Applies feature processors based on the features defined in the template context.
     *
     * @param context The template context.
     */
    private fun applyProcessors(context: TemplateContext) {
        context.layer.features.forEach { feature ->
            processorsById[feature.id]?.apply(context)
        }
    }

    /**
     * Applies dependencies specified in the processor's `dependencies` method.
     *
     * @param context The template context.
     */
    private fun applyDependencies(context: TemplateContext) {
        dependencies()
                .map(this::getFeatureProcessor)
                .onEach { processor -> processor.apply(context) }
    }

    /**
     * Removes processors associated with the processor.
     *
     * @param context The template context.
     */
    private fun removeProcessors(context: TemplateContext) {
        providerList.forEach { provider ->
            provider.getProcessors()
                    .forEach { processor -> processor.remove(context) }
        }
    }

    /**
     * Abstract method to be implemented by subclasses for performing processor-specific preparations.
     *
     * @param state The template state.
     */
    protected abstract fun prepare(state: TemplateState)

    /**
     * Abstract method to be implemented by subclasses for creating a list of feature providers.
     *
     * @return A list of feature providers.
     */
    protected abstract fun createProviders(): List<FeatureProvider>

}