package kotli.flow

import kotli.engine.TemplateState
import org.slf4j.LoggerFactory

/**
 * TemplateFlow is responsible for the generation of the required template based on some input context.
 *
 * The implementations specify such context.
 */
abstract class TemplateFlow {

    protected val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Proceeds with the template generation and returns the resulting state.
     */
    abstract suspend fun proceed(): TemplateState

}