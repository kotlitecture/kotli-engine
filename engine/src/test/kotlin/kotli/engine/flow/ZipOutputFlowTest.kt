package kotli.engine.flow

import kotli.engine.DefaultTemplateRegistry
import kotli.engine.TemplateGenerator
import kotli.engine.model.Layer
import kotli.flow.FileOutputFlow
import kotli.flow.ZipOutputFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.test.Test

class ZipOutputFlowTest {

    @Test
    fun `proceed without extra files inside`() = runBlocking {
        val output = ByteArrayOutputStream()
        val registry = DefaultTemplateRegistry(emptyList())
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            generatorId = TemplateGenerator.App.getId()
        )
        val defaultFlow = FileOutputFlow(layer, registry)
        val zipFlow = ZipOutputFlow(defaultFlow, output)
        val context = zipFlow.proceed()
        Assertions.assertEquals(22, output.size())
        Assertions.assertEquals(1, Files.walk(context.layerPath).toList().size)
    }

}