package kotli.flow

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import kotli.engine.TemplateContext
import kotli.engine.TemplateRegistry
import kotli.engine.model.Layer
import java.nio.file.Path

/**
 * Generates output structure in-memory.
 */
class DefaultTemplateFlow(
    private val layer: Layer,
    private val registry: TemplateRegistry
) : TemplateFlow() {

    override fun proceed(): TemplateContext {
        val target: Path = Jimfs.newFileSystem(Configuration.unix()).getPath("/")
        val context = DefaultTemplateContext(layer, target, registry)
        context.generator.generate(context)
        return context
    }

}