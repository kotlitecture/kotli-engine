package kotli.engine.flow

import kotli.engine.DefaultTemplateRegistry
import kotli.engine.TemplateGenerator
import kotli.engine.model.Layer
import kotli.flow.FileOutputFlow
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import java.nio.file.Files
import kotlin.test.Test

class FileOutputFlowTest {

    @Test
    fun `proceed without extra files inside`() = runBlocking {
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

    @Test
    fun `proceed with unknown child layer`() = runBlocking {
        val registry = DefaultTemplateRegistry(emptyList())
        val child1 = Layer(
            id = "child-1",
            name = "child-1",
            namespace = "child1",
            generatorId = TemplateGenerator.App.getId()
        )
        val child2 = Layer(
            id = "child-2",
            name = "child-2",
            namespace = "child2",
            generatorId = "unknown"
        )
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            generatorId = TemplateGenerator.App.getId(),
            layers = listOf(
                child1,
                child2
            )
        )
        val flow = FileOutputFlow(layer, registry)
        val context = flow.proceed()
        Assertions.assertEquals(1, context.getChildren().size)
        Assertions.assertEquals(child1, context.getChildren().first().layer)
    }

    @Test
    fun `proceed with duplicate child layer`() = runBlocking {
        val registry = DefaultTemplateRegistry(emptyList())
        val child1 = Layer(
            id = "child-1",
            name = "child-1",
            namespace = "child1",
            generatorId = TemplateGenerator.App.getId()
        )
        val child2 = Layer(
            id = "child-2",
            name = child1.name,
            namespace = "child2",
            generatorId = TemplateGenerator.App.getId()
        )
        val layer = Layer(
            id = "my.app",
            name = "test",
            namespace = "test.app",
            generatorId = TemplateGenerator.App.getId(),
            layers = listOf(
                child1,
                child2
            )
        )
        val flow = FileOutputFlow(layer, registry)
        try {
            flow.proceed()
            Assertions.fail()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}