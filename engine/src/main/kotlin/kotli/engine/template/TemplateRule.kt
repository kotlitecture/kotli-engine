package kotli.engine.template

import kotli.engine.extensions.takeIfIndex
import org.slf4j.LoggerFactory

abstract class TemplateRule {

    protected val logger = LoggerFactory.getLogger(this::class.java)

    fun apply(file: TemplateFile) {
        logger.debug("apply rule :: {} -> {}", javaClass.simpleName, file.path)
        doApply(file)
    }

    protected fun isMarked(file: TemplateFile, line: String, marker: String): Boolean {
        return line.contains(marker, ignoreCase = true)
    }

    protected fun cleanup(file: TemplateFile, line: String): String {
        val startIndex = line.indexOfAny(file.markers).takeIfIndex() ?: return line
        val newLine = line.substring(0, startIndex).trimEnd()
        logger.debug("cleanup:\n\t{}\n\t{}", line, newLine)
        return newLine
    }

    abstract fun doApply(file: TemplateFile)

}