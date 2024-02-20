package kotli.engine.extensions

import kotli.engine.TemplateGenerator
import kotli.engine.Kotli
import kotli.engine.model.Feature
import org.jetbrains.annotations.TestOnly

/**
 * Creates list with all features.
 */
@TestOnly
fun TemplateGenerator.getAllFeatures(): List<Feature> {
    return getProviders()
        .map { it.getProcessors() }
        .flatten()
        .map { Feature(id = it.getId()) }
}

/**
 * Proceeds with template generation and execution of some commands in the root directory of the template generated.
 */
@TestOnly
fun Kotli.generateAndExec(vararg commands: String) {
    generate()
    exec(target, *commands)
}

/**
 * Proceeds with template generation and execution of some gradle commands in the root directory of the template generated.
 */
@TestOnly
fun Kotli.generateAndGradlew(vararg commands: String) {
    if (!isWindows()) runCatching { generateAndExec("chmod", "-R", "777", "gradlew") }
    generateAndExec(gradlew(), *commands)
}