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
 * Execution context for one time generation of a template.
 *
 * Once this context is executed
 */
data class TemplateContext(
    val layer: Layer,
    val parent: TemplateContext? = null,
    val target: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/")
) {

    internal val applied: MutableSet<IFeatureProcessor> = mutableSetOf()
    private val generated = AtomicBoolean(false)

    /**
     * Generates template based on the given context.
     */
    fun generate(): TemplateContext {
        if (generated.compareAndSet(false, true)) {
            layer.generator.generate(this)
        }
        return this
    }

    /**
     * Generates template into the #stream provided.
     */
    fun generateAndZip(stream: OutputStream): TemplateContext {
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
        return this
    }

    /**
     * Applies template engine to the #contextPath relative to the root of the target folder.
     */
    fun apply(contextPath: String, block: TemplateMaker.() -> Unit) {
        val maker = TemplateMaker(target.resolve(contextPath))
        maker.block()
        maker.apply()
    }

    /**
     * Applies template engine to the 'gradle/libs.versions.toml' in the root of the target folder.
     */
    fun applyVersionCatalog(block: TemplateMaker.() -> Unit) {
        apply("gradle/libs.versions.toml", block)
    }

    companion object {
        val Empty = TemplateContext(
            target = Path.of("/"),
            layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generator = ITemplateGenerator.App
            )
        )
    }
}