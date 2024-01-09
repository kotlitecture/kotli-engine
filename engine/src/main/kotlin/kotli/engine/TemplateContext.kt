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

data class TemplateContext(
    val layer: Layer,
    val parent: TemplateContext? = null,
    val path: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/")
) {

    val applied: MutableSet<IFeatureProcessor> = mutableSetOf()
    private val generated = AtomicBoolean(false)

    fun generate(): TemplateContext {
        if (generated.compareAndSet(false, true)) {
            layer.generator.generate(this)
        }
        return this
    }

    fun generateAndZip(stream: OutputStream): TemplateContext {
        generate()
        val zip = ZipOutputStream(stream)
        zip.use { zipOutput ->
            Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    val relativePath: Path = path.relativize(file)
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

    fun generateAndExec(vararg commands: String) {
        generate()
        val args = if (isWindows()) {
            mutableListOf("cmd.exe", "/C")
        } else {
            mutableListOf()
        }
        args.addAll(commands.toList())
        val dir = path
        val process = ProcessBuilder()
            .directory(dir.toFile())
            .command(args)
            .inheritIO()
            .start()
        val exitCode = process.waitFor()
        if (exitCode != 0) throw IllegalStateException("wrong exit code $exitCode")
    }

    fun generateAndGradlew(vararg commands: String) {
        if (!isWindows()) {
            runCatching { generateAndExec("chmod", "-R", "777", "gradlew") }
        }
        generateAndExec(gradlew(), *commands)
    }

    fun apply(contextPath: String, block: TemplateMaker.() -> Unit) {
        val maker = TemplateMaker(path.resolve(contextPath))
        maker.block()
        maker.apply()
    }

    fun applyVersionCatalog(block: TemplateMaker.() -> Unit) {
        apply("gradle/libs.versions.toml", block)
    }

    private fun isWindows(): Boolean {
        return try {
            System.getProperty("os.name").lowercase().startsWith("windows")
        } catch (e: SecurityException) {
            false
        }
    }

    private fun gradlew(): String {
        if (isWindows()) {
            return "gradlew"
        } else {
            return "./gradlew"
        }
    }

    companion object {
        val Empty = TemplateContext(
            path = Path.of("/"),
            layer = Layer(
                id = "<YOUR_LAYER_ID>",
                name = "<YOUR_LAYER_NAME>",
                namespace = "<YOUR_LAYER_NAMESPACE>",
                generator = ITemplateGenerator.App
            )
        )
    }
}