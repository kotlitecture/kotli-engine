package kotli.engine.flow

import kotli.engine.DefaultTemplateRegistry
import kotli.engine.TemplateGenerator
import kotli.engine.model.Layer
import kotli.flow.FileOutputFlow
import org.junit.jupiter.api.Assertions
import java.nio.file.Files
import kotlin.test.Test

class FileOutputFlowTest {

    @Test
    fun `proceed without extra files inside`() {
        val registry = DefaultTemplateRegistry(emptyList())
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            generatorId = TemplateGenerator.App.getId()
        )
        val flow = FileOutputFlow(layer, registry)
        val context = flow.proceed()
        Assertions.assertEquals(1, Files.walk(context.layerPath).toList().size)
    }

}