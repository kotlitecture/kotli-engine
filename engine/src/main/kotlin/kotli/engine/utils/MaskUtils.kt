package kotli.engine.utils

/**
 * Utility class for converting mask patterns to regular expressions.
 */
object MaskUtils {

    /**
     * Converts a mask pattern to a regular expression pattern.
     *
     * @param mask The mask pattern to convert.
     * @return The regular expression pattern equivalent to the mask.
     */
    fun toRegex(mask: String): Regex {
        val regex = StringBuilder("^")
        for (i in mask.indices) {
            when (val char = mask[i]) {
                '*' -> regex.append(".*")
                '.' -> regex.append("\\.")
                '?' -> regex.append(".")
                else -> regex.append(char)
            }
        }
        regex.append('$')
        return regex.toString().toRegex()
    }

    /**
     * Checks if the given string is a mask, which typically contains wildcard characters such as '*' or '?'.
     *
     * @param string The string to check.
     * @return true if the string is a mask, false otherwise.
     */
    fun isMask(string: String): Boolean {
        return string.contains("*") || string.contains("?")
    }

}