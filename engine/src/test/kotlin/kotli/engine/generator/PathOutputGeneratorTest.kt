package kotli.engine.generator

import kotli.engine.DefaultTemplateRegistry
import kotli.engine.TemplateProcessor
import kotli.engine.model.Layer
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import java.nio.file.Files
import kotlin.test.Test

class PathOutputGeneratorTest {

    private val registry = DefaultTemplateRegistry()

    @Test
    fun `generate without extra files inside`() = runBlocking {
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            processorId = TemplateProcessor.App.getId()
        )
        val generator = PathOutputGenerator(registry = registry)
        val state = generator.generate(layer)
        Assertions.assertTrue(state.getChildren().isEmpty())
        Assertions.assertEquals(3, Files.walk(generator.output).toList().size)
    }

    @Test
    fun `generate with unknown child layer`() = runBlocking {
        val child1 = Layer(
            id = "child-1",
            name = "child-1",
            namespace = "child1",
            processorId = TemplateProcessor.App.getId()
        )
        val child2 = Layer(
            id = "child-2",
            name = "child-2",
            namespace = "child2",
            processorId = "unknown"
        )
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            processorId = TemplateProcessor.App.getId(),
            layers = listOf(
                child1,
                child2
            )
        )
        val generator = PathOutputGenerator(registry = registry)
        val state = generator.generate(layer)
        Assertions.assertEquals(1, state.getChildren().size)
        Assertions.assertEquals(child1, state.getChildren().first().layer)
    }

    @Test
    fun `generate with duplicate child layer`() = runBlocking {
        val child1 = Layer(
            id = "child-1",
            name = "child-1",
            namespace = "child1",
            processorId = TemplateProcessor.App.getId()
        )
        val child2 = Layer(
            id = "child-2",
            name = child1.name,
            namespace = "child2",
            processorId = TemplateProcessor.App.getId()
        )
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            processorId = TemplateProcessor.App.getId(),
            layers = listOf(
                child1,
                child2
            )
        )
        val generator = PathOutputGenerator(registry = registry)
        try {
            generator.generate(layer)
            Assertions.fail()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}