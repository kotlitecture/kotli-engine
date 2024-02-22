package kotli.engine

import org.slf4j.LoggerFactory

/**
 * The Feature processor is responsible for the inclusion or exclusion of the feature it implements
 * in the generated template.
 *
 * A feature is any atomic integration, technical solution, or business flow
 * that can be added to a layer during its configuration in Kotli.
 *
 * Each feature should be self-descriptive, allowing it to be presented to the user with an icon, title, description, links,
 * and any other metadata required to understand its value and purpose.
 *
 * The primary advantage of a feature is to provide a ready-to-use solution with minimal configuration required
 * (zero configuration is the goal).
 *
 * By default, each template is pre-configured to include all features.
 */
interface FeatureProcessor : DependencyProvider<FeatureProcessor>, FeatureDescriptor {

    /**
     * Applies the given processor to the generated template.
     *
     * @param context The current runtime template context with user-defined parameters.
     */
    fun apply(context: TemplateContext) = Unit

    /**
     * Removes the given processor from the generated template.
     *
     * @param context The current runtime template context with user-defined parameters.
     */
    fun remove(context: TemplateContext) = Unit

    /**
     * Default implementation of the processor to be called in case the processor is not found for some reason.
     */
    class UnknownProcessor(private val id: String) : FeatureProcessor {

        private val logger = LoggerFactory.getLogger(UnknownProcessor::class.java)

        override fun getId(): String = id

        override fun apply(context: TemplateContext) {
            logger.debug("apply unknown processor :: {}", id)
        }

        override fun remove(context: TemplateContext) {
            logger.debug("remove unknown processor :: {}", id)
        }
    }

}