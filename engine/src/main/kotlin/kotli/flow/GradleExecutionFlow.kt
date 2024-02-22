package kotli.flow

import kotli.engine.TemplateState
import kotli.engine.extensions.exec
import kotli.engine.extensions.gradlew

/**
 * Proceeds with template generation and execution of some command line commands in the root directory of the output structure.
 *
 * The commands will be executed as arguments for the command './gradlew'.
 */
class GradleExecutionFlow(
    private val flow: TemplateFlow,
    private val commands: Array<String>
) : TemplateFlow() {

    override fun proceed(): TemplateState {
        val state = flow.proceed()
        runCatching { exec(state.layerPath, "chmod", "-R", "777", "gradlew") }
        exec(state.layerPath, gradlew(), *commands)
        return state
    }

}