package kotli.flow

import kotli.engine.TemplateContext
import java.io.OutputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * Saves generated output structure into output stream as a zip archive.
 */
class ZipTemplateFlow(
    private val flow: TemplateFlow,
    private val output: OutputStream,
) : TemplateFlow() {

    override fun proceed(): TemplateContext {
        val context = flow.proceed()
        val zip = ZipOutputStream(output)
        zip.use { zipOutput ->
            val target = context.target
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
        return context
    }

}