package kotli.engine.template

/**
 * Describes a set of #rules which will be applied to the given #filePath.
 *
 * @param contextPath The path to the file relative to the template root context.
 * @param rules The list of rules to be applied to the file.
 */
open class FileRules(
        open val contextPath: String,
        open val rules: List<FileRule>,
        open val markerSeparators: List<String> = MARKER_SEPARATORS
) {
    companion object {
        val MARKER_SEPARATORS = listOf("//", "#")
    }
}