package kotli.flow

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.TemplateContext
import kotli.engine.TemplateRegistry
import kotli.engine.model.Feature
import kotli.engine.model.Layer
import kotli.engine.template.TemplateFile
import kotli.engine.utils.PathUtils
import java.nio.file.Files
import java.nio.file.Path

/**
 * Generates output structure in the given folder #layerPath.
 *
 * By default the target folder is set as Jimfs (in-memory file structure implementation) root directory.
 *
 * @param layer layer to generate template for
 * @param registry registry with all known template generators
 * @param layerPath root folder where to generate the result structure
 * @param fatLayer - include or not all features ignoring the fact that layer can be preconfigured with some features
 */
class FileOutputFlow(
    private val layer: Layer,
    private val registry: TemplateRegistry,
    private val layerPath: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/"),
    private val fatLayer: Boolean = false
) : TemplateFlow() {

    override fun proceed(): TemplateContext {
        // prepare context
        val context = DefaultTemplateContext(
            layer = provideLayer(),
            registry = registry,
            layerPath = layerPath,
        )
        context.generator.prepare(context)

        // generate structure
        generate(context)

        // cleanup structure
        Files.walk(layerPath)
            .filter(PathUtils::isEmptyDir)
            .forEach(PathUtils::delete)

        // return result
        return context
    }

    private fun generate(context: TemplateContext) {
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

}