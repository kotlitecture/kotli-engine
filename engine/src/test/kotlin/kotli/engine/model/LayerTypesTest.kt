package kotli.engine.model

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class LayerTypesTest {

    @Test
    fun `layer types have icon, title and description`() {
        LayerTypes.entries
            .filter { it.getTitle() != null }
            .also { Assertions.assertEquals(4, it.size) }
            .forEach { type ->
                Assertions.assertNotNull(type.getTitle(), type.getId())
            }
    }

}