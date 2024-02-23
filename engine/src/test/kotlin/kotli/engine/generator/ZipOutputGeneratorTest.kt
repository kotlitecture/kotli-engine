package kotli.engine.generator

import kotli.engine.DefaultTemplateRegistry
import kotli.engine.TemplateProcessor
import kotli.engine.model.Layer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.test.Test

class ZipOutputGeneratorTest {

    val registry = DefaultTemplateRegistry(emptyList())

    @Test
    fun `generate without extra files inside`() = runBlocking {
        val layer = Layer(
                id = "my.app",
                name = "test",
                namespace = "test.app",
                processorId = TemplateProcessor.App.getId()
        )
        val output = ByteArrayOutputStream()
        val pathGenerator = PathOutputGenerator(registry = registry)
        val zipGenerator = ZipOutputGenerator(output, pathGenerator)
        val state = zipGenerator.generate(layer)
        Assertions.assertEquals(22, output.size())
        Assertions.assertEquals(1, Files.walk(state.layerPath).toList().size)
    }

}