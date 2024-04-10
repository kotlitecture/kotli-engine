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

    private val registry = DefaultTemplateRegistry()

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
        Assertions.assertTrue(output.size() > 400)
        Assertions.assertTrue(state.getChildren().isEmpty())
        Assertions.assertEquals(3, Files.walk(pathGenerator.output).toList().size)
    }

}