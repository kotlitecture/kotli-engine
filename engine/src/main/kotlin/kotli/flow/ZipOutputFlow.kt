package kotli.flow

import kotli.engine.TemplateState
import java.io.OutputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Saves the generated output structure into the output stream as a zip archive.
 */
class ZipOutputFlow(
    private val flow: TemplateFlow,
    private val output: OutputStream,
) : TemplateFlow() {

    override suspend fun proceed(): TemplateState {
        val state = flow.proceed()
        val zip = ZipOutputStream(output)
        zip.use { zipOutput ->
            val target = state.layerPath
            Files.walkFileTree(target, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val relativePath: Path = target.relativize(file)
                    val zipEntry = ZipEntry(relativePath.toString())
                    zipOutput.putNextEntry(zipEntry)
                    Files.copy(file, zipOutput)
                    zipOutput.closeEntry()
                    return FileVisitResult.CONTINUE
                }
            })
        }
        return state
    }

}