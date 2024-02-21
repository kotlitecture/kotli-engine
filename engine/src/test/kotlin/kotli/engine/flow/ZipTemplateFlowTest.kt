package kotli.engine.flow

import kotli.engine.SimpleTemplateRegistry
import kotli.engine.TemplateGenerator
import kotli.engine.model.Layer
import kotli.flow.DefaultTemplateFlow
import kotli.flow.ZipTemplateFlow
import org.junit.jupiter.api.Assertions
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.test.Test

class ZipTemplateFlowTest {

    @Test
    fun `proceed without extra files inside`() {
        val output = ByteArrayOutputStream()
        val registry = SimpleTemplateRegistry(emptyList())
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            generatorId = TemplateGenerator.App.getId()
        )
        val defaultFlow = DefaultTemplateFlow(layer, registry)
        val zipFlow = ZipTemplateFlow(defaultFlow, output)
        val context = zipFlow.proceed()
        Assertions.assertEquals(22, output.size())
        Assertions.assertEquals(1, Files.walk(context.target).toList().size)
    }

}