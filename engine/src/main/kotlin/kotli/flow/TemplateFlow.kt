package kotli.flow

import kotli.engine.TemplateContext
import org.slf4j.LoggerFactory

/**
 * Kotli flow is responsible for generation of the required template based on some input context.
 *
 * The implementations specify such context.
 */
abstract class TemplateFlow {

    protected val logger = LoggerFactory.getLogger(this::class.java)

    abstract fun proceed(): TemplateContext

}