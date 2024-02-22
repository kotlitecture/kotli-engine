package kotli.engine

import org.slf4j.LoggerFactory

/**
 * Feature processor is responsible for inclusion or exclusion of the feature it implements
 * in the generated template.
 *
 * Feature is any atomic integrations, technical solutions or business flows
 * which can be added to a layer during its configuration in Kotli.
 *
 * Each feature should be self-describable so it can be presented to the user with icon, title, description, links
 * and any other metadata required to understand its value and purpose.
 *
 * The main advantage of a feature is to provide ready-to-use solution with a minimum configuration required
 * (zero configuration is a goal).
 *
 * By default, each template is pre-configured to include all features.
 */
interface FeatureProcessor : DependencyProvider<FeatureProcessor>, FeatureDescriptor {

    /**
     * Applies given processor to the template generated.
     *
     * @param context is current runtime template context with user defined parameters.
     */
    fun apply(context: TemplateContext) = Unit

    /**
     * Removes given processor from the template generated.
     *
     * @param context is current runtime template context with user defined parameters.
     */
    fun remove(context: TemplateContext) = Unit

    /**
     * Default implementation of processor which should be called in case if processor not found by some reasons.
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