package kotli.engine.extensions

import java.nio.file.Path

/**
 * Checks if current system is windows or not.
 */
fun isWindows(): Boolean {
    return runCatching {
        System.getProperty("os.name").lowercase().startsWith("windows")
    }.getOrElse { false }
}

/**
 * Choose the right gradle script to run based on the system.
 */
fun gradlew(): String {
    return if (isWindows()) "gradlew" else "./gradlew"
}

/**
 * Executes given set of #commands in the directory #dir.
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
 * Takes given Int value if it is positive and can be an index in array.
 */
fun Int.takeIfIndex(): Int? = this.takeIf { it >= 0 }

/**
 * Constructs path from multiple tokens.
 */
fun path(vararg tokens: String): String = tokens.joinToString("/").replace("//", "/").trim()