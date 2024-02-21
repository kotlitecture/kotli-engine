package kotli.flow

import kotli.engine.TemplateContext
import kotli.engine.extensions.exec
import kotli.engine.extensions.gradlew

/**
 * Proceeds with template generation and execution of some command line commands in the root directory of the output structure.
 *
 * The commands will be executed as arguments for the command './gradlew'.
 */
class GradleCmdTemplateFlow(
    private val flow: TemplateFlow,
    private val commands: Array<String>
) : TemplateFlow() {

    override fun proceed(): TemplateContext {
        val context = flow.proceed()
        runCatching { exec(context.target, "chmod", "-R", "777", "gradlew") }
        exec(context.target, gradlew(), *commands)
        return context
    }

}