package kotli.engine.generator

import kotli.engine.TemplateGenerator
import kotli.engine.TemplateState
import kotli.engine.extensions.exec
import kotli.engine.extensions.execSilently
import kotli.engine.extensions.gradlew
import kotli.engine.extensions.isWindows
import kotli.engine.model.Layer

/**
 * Proceeds with template generation and execution of command-line commands in the root directory of the output structure,
 * assuming the output structure is a Gradle project. The commands are executed as arguments for the './gradlew' command,
 * and the result state is returned only after successful execution of the commands.
 *
 * @param generator The template generator responsible for generating the output structure.
 * @param commands An array of command-line commands to be executed in the root directory of the output structure.
 */
class GradleProjectGenerator(
    private val commands: Array<String>,
    private val generator: PathOutputGenerator,
) : TemplateGenerator() {

    override suspend fun generate(layer: Layer): TemplateState {
        val state = generator.generate(layer)
        if (!isWindows()) execSilently(generator.output, "chmod", "-R", "777", "gradlew")
        exec(generator.output, gradlew(), *commands)
        return state
    }

}