package kotli.engine.generator

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.DefaultTemplateContext
import kotli.engine.TemplateGenerator
import kotli.engine.TemplateRegistry
import kotli.engine.TemplateState
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateFile
import kotli.engine.utils.PathUtils
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates the output structure in the given folder.
 *
 * By default, the target folder is set as the root directory in an in-memory file structure implementation.
 *
 * @param output The root folder where to generate the result structure.
 * @param registry Registry containing all known template generators.
 * @param fat Whether to include all features or not, ignoring the fact that the layer can be preconfigured with some features.
 */
class PathOutputGenerator(
    private val output: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/"),
    private val registry: TemplateRegistry,
    private val fat: Boolean = false
) : TemplateGenerator() {

    override suspend fun generate(layer: Layer): TemplateState {
        val state = prepare(adjustLayer(layer))
        generate(state)
        cleanup(state)
        return state
    }

    /**
     * Adjusts the layer based on the fat flag, including additional features if necessary.
     */
    private fun adjustLayer(layer: Layer): Layer {
        if (fat) {
            val processor = registry.get(layer.processorId)!!
            val features = processor.getProviders()
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
        val context = DefaultTemplateContext(
            registry = registry,
            layerPath = output,
            layer = layer
        )
        context.processor.process(context)
        return context
    }

    /**
     * Generates the template based on the provided context.
     */
    private fun generate(context: TemplateState) {
        val templatePath = context.processor.getTemplatePath()
        val from = PathUtils.getFromResource(templatePath) ?: return
        val to = context.layerPath
        PathUtils.copy(from, to)
        context.getRules()
            .groupBy { it.filePath }
            .forEach { group ->
                val templateFile = TemplateFile(group.key)
                group.value
                    .map { it.rules }
                    .flatten()
                    .forEach { rule -> rule.apply(templateFile) }
                logger.debug("update file :: {}", templateFile.path)
                templateFile.write()
            }
        context.getChildren().onEach(this::generate)
    }

    /**
     * Cleans up any generated files or directories that are empty.
     */
    private fun cleanup(context: TemplateState) {
        Files.walk(context.layerPath)
            .filter(PathUtils::isEmptyDir)
            .forEach(PathUtils::delete)
    }

}