package kotli.engine

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.model.Layer
import java.io.OutputStream
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.concurrent.atomic.AtomicBoolean
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

/**
 * The entry point to generate output structure based on the metadata provided.
 */
data class Kotli(
        val layer: Layer,
        val registry: ITemplateRegistry,
        val target: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/"),
) {

    private val generated = AtomicBoolean(false)

    /**
     * Generates template based on the given context.
     */
    fun generate() {
        if (generated.compareAndSet(false, true)) {
            val context = TemplateContext(layer, target, registry)
            context.generator.generate(context)
        }
    }

    /**
     * Generates template into the #stream provided.
     */
    fun generate(stream: OutputStream) {
        generate()
        val zip = ZipOutputStream(stream)
        zip.use { zipOutput ->
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
    }
}