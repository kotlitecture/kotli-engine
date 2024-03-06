package kotli.engine.generator

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.DefaultTemplateContext
import kotli.engine.TemplateContext
import kotli.engine.TemplateGenerator
import kotli.engine.TemplateRegistry
import kotli.engine.TemplateState
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateFile
import kotli.engine.utils.PathUtils
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.writeLines

/**
 * Generates the output structure in the given folder.
 *
 * By default, the target folder is set as the root directory in an in-memory file structure implementation.
 *
 * @param output The root folder where to generate the result structure.
 * @param registry Registry containing all known template generators.
 * @param fat Whether to include all features or not, ignoring the fact that the layer can be preconfigured with some features.
 */
open class PathOutputGenerator(
    val output: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/"),
    private val registry: TemplateRegistry,
    private val fat: Boolean = false
) : TemplateGenerator() {

    override suspend fun generate(layer: Layer): TemplateState {
        val state = prepare(adjustLayer(layer))
        generate(state)
        cleanup()
        return state
    }

    protected open fun createContext(layer: Layer): TemplateContext {
        return DefaultTemplateContext(
            contextPath = layer.name,
            registry = registry,
            layer = layer
        )
    }

    /**
     * Adjusts the layer based on the fat flag, including additional features if necessary.
     */
    private fun adjustLayer(layer: Layer): Layer {
        if (fat) {
            val processor = registry.get(layer.processorId)!!
            val features = processor.getFeatureProviders()
                .map { it.getProcessors() }
                .flatten()
                .map { Feature(id = it.getId()) }
            return layer.copy(features = features)
        }
        return layer
    }

    /**
     * Prepares the template generation context.
     */
    private suspend fun prepare(layer: Layer): TemplateState {
        val context = createContext(layer)
        context.processor.process(context)
        return context
    }

    /**
     * Generates the template based on the provided context.
     */
    private fun generate(state: TemplateState) {
        val to = output.resolve(state.contextPath)
        val templatePath = state.processor.getTemplatePath()
        PathUtils.getFromResource(templatePath)?.let { PathUtils.copy(it, to) }
        state.getRules()
            .groupBy { it.contextPath }
            .forEach { group ->
                val templateFile = TemplateFile(
                    path = to.resolve(group.key),
                    markerSeparators = group.value
                        .map { it.markerSeparators }
                        .flatten()
                        .distinct()
                )
                group.value
                    .map { it.rules }
                    .flatten()
                    .forEach { rule -> rule.apply(templateFile) }
                logger.debug("update file :: {}", templateFile.path)
                write(templateFile)
            }
        state.getChildren().onEach(this::generate)
    }

    private fun write(file: TemplateFile) {
        when {
            file.path.isDirectory() -> return
            file.lines.isNotEmpty() -> {
                if (!file.path.parent.exists()) {
                    file.path.parent.createDirectories()
                }
                file.path.writeLines(file.lines)
            }

            else -> file.path.deleteIfExists()
        }
    }

    /**
     * Cleans up any generated files or directories that are empty.
     */
    private fun cleanup() {
        Files.walk(output)
            .filter(PathUtils::isEmptyDir)
            .forEach(PathUtils::delete)
    }

}