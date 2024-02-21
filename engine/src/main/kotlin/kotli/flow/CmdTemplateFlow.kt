package kotli.flow

import kotli.engine.TemplateContext
import kotli.engine.extensions.exec

/**
 * Proceeds with template generation and execution of some command line commands in the root directory of the output structure.
 */
class CmdTemplateFlow(
    private val flow: TemplateFlow,
    private val commands: Array<String>
) : TemplateFlow() {

    override fun proceed(): TemplateContext {
        val context = flow.proceed()
        exec(context.layerPath, *commands)
        return context
    }

}