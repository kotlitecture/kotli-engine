package kotli.flow

import kotli.engine.TemplateState
import kotli.engine.extensions.exec

/**
 * Proceeds with template generation and execution of some command line commands in the root directory of the output structure.
 */
class CommandExecutionFlow(
    private val flow: TemplateFlow,
    private val commands: Array<String>
) : TemplateFlow() {

    override suspend fun proceed(): TemplateState {
        val state = flow.proceed()
        exec(state.layerPath, *commands)
        return state
    }

}