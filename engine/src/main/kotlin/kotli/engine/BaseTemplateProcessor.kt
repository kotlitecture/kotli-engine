package kotli.engine

import kotli.engine.model.Feature
import kotli.engine.model.Layer
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
     * Lazily initialized map of processors indexes in the order of their registration.
     */
    private val processorsOrderById by lazy {
        providerList.map { provider -> provider.getProcessors() }
            .flatten()
            .mapIndexed { index, featureProcessor -> featureProcessor.getId() to index }
            .toMap()
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

    override fun getFeatureProviders(): List<FeatureProvider> {
        return providerList
    }

    override fun getFeatureProcessor(id: String): FeatureProcessor {
        return processorsById[id] ?: FeatureProcessor.Unknown(id)
    }

    override fun getFeatureProcessorOrder(id: String): Int {
        return processorsOrderById[id] ?: -1
    }

    override fun getFeatureProcessor(type: Class<out FeatureProcessor>): FeatureProcessor {
        return processorsByType[type] ?: throw IllegalStateException("no processor :: $type")
    }

    override fun getFeatureProvider(type: Class<out FeatureProcessor>): FeatureProvider {
        return providersByProcessorType[type] ?: throw IllegalStateException("no provider :: $type")
    }

    /**
     * Prepares the template context by executing the necessary steps, including:
     * 1. Invoking the `processBefore` method.
     * 2. Processing child layers.
     * 3. Applying feature processors.
     * 4. Applying dependencies.
     * 5. Removing processors.
     * 6. Invoking the `processAfter` method.
     *
     * @param context The template context.
     */
    override suspend fun process(context: TemplateContext) {
        processBefore(context)
        proceedChildren(context)
        applyProcessors(context)
        applyDependencies(context)
        removeProcessors(context)
        processAfter(context)
    }

    final override fun getPresets(): List<Layer> {
        val presets = createPresets().toMutableList()
        val requiredProviders = providerList
            .filter { provider -> provider.isRequired() }
            .filter { provider -> provider.getProcessors().any { !it.isInternal() } }
        presets.forEachIndexed { index, preset ->
            val presetProviders = preset.features
                .map { getFeatureProcessor(it.id) }
                .map { getFeatureProvider(it::class.java) }
            val missedFeatures = requiredProviders
                .minus(presetProviders.toSet())
                .mapNotNull { provider -> provider.getProcessors().firstOrNull { !it.isInternal() } }
                .map { Feature(it.getId()) }
            if (missedFeatures.isNotEmpty()) {
                val features = preset.features.plus(missedFeatures)
                presets[index] = preset.copy(features = features)
            }
        }
        if (presets.isEmpty()) {
            val features = requiredProviders
                .mapNotNull { provider -> provider.getProcessors().firstOrNull { !it.isInternal() } }
                .map { Feature(it.getId()) }
            presets.add(
                Layer(
                    id = "",
                    name = "",
                    namespace = "com.example.${getType().getId()}",
                    processorId = getId(),
                    features = features
                )
            )
        }
        return presets
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
        context.layer.features
            .sortedBy { getFeatureProcessorOrder(it.id) }
            .forEach { feature -> processorsById[feature.id]?.apply(context) }
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
     * Logic to be implemented by subclasses for performing processor-specific preparations before applying features.
     *
     * @param state The template state.
     */
    protected open fun processBefore(state: TemplateState) {
        logger.debug("processBefore :: {}", state)
    }

    /**
     * Logic to be implemented by subclasses for performing processor-specific preparations after applying features.
     *
     * @param state The template state.
     */
    protected open fun processAfter(state: TemplateState) {
        logger.debug("processAfter :: {}", state)
    }

    /**
     * Creates presets for the given template.
     */
    protected open fun createPresets(): List<Layer> = emptyList()

    /**
     * Abstract method to be implemented by subclasses for creating a list of feature providers.
     *
     * @return A list of feature providers.
     */
    protected abstract fun createProviders(): List<FeatureProvider>

}