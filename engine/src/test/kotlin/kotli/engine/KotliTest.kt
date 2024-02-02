package kotli.engine

import kotli.engine.model.Layer
import org.junit.jupiter.api.Assertions
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.test.Test

class KotliTest {

    @Test
    fun `generate without extra files inside`() {
        val kotli = Kotli(
            layer = Layer(
                id = "my.app",
                name = "test",
                namespace = "test.app",
                generator = ITemplateGenerator.App
            )
        )
        kotli.generate()
        Assertions.assertEquals(1, Files.walk(kotli.target).toList().size)
    }

    @Test
    fun `generate and zip without extra files inside`() {
        val out = ByteArrayOutputStream()
        val kotli = Kotli(
            layer = Layer(
                id = "my.app",
                name = "test",
                namespace = "test.app",
                generator = ITemplateGenerator.App
            )
        )
        kotli.generateAndZip(out)
        Assertions.assertEquals(22, out.size())
        Assertions.assertEquals(1, Files.walk(kotli.target).toList().size)
    }

}