package kotli.engine.test

import kotli.engine.ITemplateGenerator
import kotli.engine.Kotli
import kotli.engine.model.Feature
import org.jetbrains.annotations.TestOnly

/**
 * Creates list with all features.
 */
@TestOnly
fun ITemplateGenerator.getAllFeatures(): List<Feature> {
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
    val args = if (isWindows()) {
        mutableListOf("cmd.exe", "/C")
    } else {
        mutableListOf()
    }
    args.addAll(commands.toList())
    val dir = target
    val process = ProcessBuilder()
        .directory(dir.toFile())
        .command(args)
        .inheritIO()
        .start()
    val exitCode = process.waitFor()
    if (exitCode != 0) throw IllegalStateException("wrong exit code $exitCode")
}

/**
 * Proceeds with template generation and execution of some gradle commands in the root directory of the template generated.
 */
@TestOnly
fun Kotli.generateAndGradlew(vararg commands: String) {
    if (!isWindows()) {
        runCatching { generateAndExec("chmod", "-R", "777", "gradlew") }
    }
    generateAndExec(gradlew(), *commands)
}

private fun isWindows(): Boolean = runCatching { System.getProperty("os.name").lowercase().startsWith("windows") }.getOrElse { false }
private fun gradlew(): String = if (isWindows()) "gradlew" else "./gradlew"