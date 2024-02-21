package kotli.engine.template

import java.nio.file.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.readLines
import kotlin.io.path.writeLines

data class TemplateFile(
    val path: Path,
    val markers: List<String> = MARKERS
) {
    val lines by lazy {
        if (path.exists()) {
            path.readLines().toMutableList()
        } else {
            mutableListOf()
        }
    }

    fun write() {
        if (!path.exists()) return
        if (path.isDirectory()) return
        if (lines.isNotEmpty()) {
            path.writeLines(lines)
        } else {
            path.deleteIfExists()
        }
    }

    companion object {
        val MARKERS = listOf("//", "#")
    }
}