package kotli.engine

import kotli.engine.model.Layer
import org.junit.jupiter.api.Assertions
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.test.Test

class TemplateContextTest {

    @Test
    fun `generate without extra files inside`() {
        val context = TemplateContext(
            layer = Layer(
                id = "my.app",
                name = "test",
                namespace = "test.app",
                generator = ITemplateGenerator.App
            )
        )
        context.generate()
        Assertions.assertEquals(1, Files.walk(context.target).toList().size)
    }

    @Test
    fun `generate and zip without extra files inside`() {
        val out = ByteArrayOutputStream()
        val context = TemplateContext(
            layer = Layer(
                id = "my.app",
                name = "test",
                namespace = "test.app",
                generator = ITemplateGenerator.App
            )
        )
        context.generateAndZip(out)
        Assertions.assertEquals(22, out.size())
        Assertions.assertEquals(1, Files.walk(context.target).toList().size)
    }

}