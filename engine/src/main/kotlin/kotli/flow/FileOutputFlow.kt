package kotli.flow

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.DefaultTemplateContext
import kotli.engine.TemplateRegistry
import kotli.engine.TemplateState
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateFile
import kotli.engine.utils.PathUtils
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates the output structure in the given folder #layerPath.
 *
 * By default, the target folder is set as the Jimfs (in-memory file structure implementation) root directory.
 *
 * @param layer layer to generate the template for.
 * @param registry registry with all known template generators.
 * @param layerPath root folder where to generate the result structure.
 * @param fatLayer - include or not all features, ignoring the fact that the layer can be preconfigured with some features.
 */
class FileOutputFlow(
    private val layer: Layer,
    private val registry: TemplateRegistry,
    private val layerPath: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/"),
    private val fatLayer: Boolean = false
) : TemplateFlow() {

    override suspend fun proceed(): TemplateState {
        val state = prepare()
        generate(state)
        cleanup(state)
        return state
    }

    private fun provideLayer(): Layer {
        if (fatLayer) {
            val generator = registry.get(layer.generatorId)!!
            val features = generator.getProviders()
                .map { it.getProcessors() }
                .flatten()
                .map { Feature(id = it.getId()) }
            return layer.copy(features = features)
        }
        return layer
    }

    private suspend fun prepare(): TemplateState {
        val context = DefaultTemplateContext(
            layer = provideLayer(),
            registry = registry,
            layerPath = layerPath,
        )
        context.generator.prepare(context)
        return context
    }

    private fun generate(context: TemplateState) {
        val templatePath = context.generator.getTemplatePath()
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

    private fun cleanup(context: TemplateState) {
        Files.walk(context.layerPath)
            .filter(PathUtils::isEmptyDir)
            .forEach(PathUtils::delete)
    }

}