package kotli.engine.generator

import kotli.engine.TemplateGenerator
import kotli.engine.TemplateState
import kotli.engine.model.Layer
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
 *
 * @param generator The template generator responsible for generating the output structure to be zipped.
 * @param output The output stream where the zip archive will be written.
 */
class ZipOutputGenerator(
    private val output: OutputStream,
    private val generator: PathOutputGenerator,
) : TemplateGenerator() {

    override suspend fun generate(layer: Layer): TemplateState {
        val state = generator.generate(layer)
        val zip = ZipOutputStream(output)
        zip.use { zipOutput ->
            val target = generator.output
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