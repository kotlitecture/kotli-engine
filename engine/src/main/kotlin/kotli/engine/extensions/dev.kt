package kotli.engine.extensions

import java.nio.file.Path

/**
 * Checks if the current operating system is Windows.
 *
 * @return `true` if the current operating system is Windows; otherwise, `false`.
 */
fun isWindows(): Boolean {
    return runCatching { System.getProperty("os.name").lowercase().startsWith("windows") }
        .getOrElse { false }
}

/**
 * Chooses the appropriate Gradle script to run based on the operating system.
 *
 * @return The Gradle script command ('gradlew' for Windows, './gradlew' for other systems).
 */
fun gradlew(): String {
    return if (isWindows()) "gradlew" else "./gradlew"
}

/**
 * Executes the given set of commands in the specified directory.
 *
 * @param dir The directory in which to execute the commands.
 * @param commands The list of commands to execute.
 * @throws IllegalStateException if the exit code is not 0.
 */
fun exec(dir: Path, vararg commands: String) {
    val args = if (isWindows()) mutableListOf("cmd.exe", "/C") else mutableListOf()
    args.addAll(commands.toList())
    val process = ProcessBuilder()
        .directory(dir.toFile())
        .command(args)
        .inheritIO()
        .start()
    val exitCode = process.waitFor()
    if (exitCode != 0) throw IllegalStateException("wrong exit code $exitCode")
}

/**
 * Takes the given Int value if it is positive and can be used as an index in an array.
 *
 * @return The Int value if positive and a valid index, otherwise null.
 */
fun Int.takeIfIndex(): Int? {
    return takeIf { it >= 0 }
}

/**
 * Constructs a path from multiple tokens.
 *
 * @param tokens The tokens to be joined into a path.
 * @return The constructed path with proper formatting.
 */
fun path(vararg tokens: String): String {
    return tokens.joinToString("/").replace("//", "/").trim()
}