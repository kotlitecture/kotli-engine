package kotli.engine.utils

/**
 * Utility class for string manipulation operations.
 */
object StringUtils {

    /**
     * Trims extra spaces and ensures proper line formatting.
     *
     * @param string The input string to be trimmed.
     * @return The trimmed string with proper line formatting.
     */
    fun trimLines(string: String): String {
        val result = StringBuilder(string.length)
        val lines = string.lines()
        lines.forEachIndexed { index, line ->
            val prev = lines.getOrNull(index - 1)
            if (prev != null && prev != "\n" && !prev.endsWith(".")) {
                if (!prev.endsWith(" ")) {
                    result.append(" ")
                }
                result.append(line)
            } else {
                if (index > 0) {
                    result.appendLine()
                }
                result.append(line)
            }
        }
        return result.toString()
    }

}