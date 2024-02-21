package kotli.flow

import kotli.engine.TemplateContext

/**
 * Kotli flow is responsible for generation of the required template based on some input context.
 *
 * The implementations specify such context.
 */
abstract class TemplateFlow {

    abstract fun proceed(): TemplateContext

}