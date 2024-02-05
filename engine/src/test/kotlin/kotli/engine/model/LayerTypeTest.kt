package kotli.engine.model

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class LayerTypeTest {

    @Test
    fun `layer types have icon, title and description`() {
        LayerType.entries
            .filter { it.getTitle() != null }
            .also { Assertions.assertEquals(3, it.size) }
            .forEach { type ->
                Assertions.assertNotNull(type.getTitle(), type.id)
            }
    }

}