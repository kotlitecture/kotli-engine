package kotli.engine

import kotli.engine.model.Layer
import org.slf4j.LoggerFactory

/**
 * TemplateGenerator is an abstract class responsible for generating templates to an output stream.
 */
abstract class TemplateGenerator {

    protected val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Proceeds with the template generation and returns the resulting state.
     *
     * @param layer The layer information for generating the template.
     * @return The state of the template generation process.
     */
    abstract suspend fun generate(layer: Layer): TemplateState

}