package kotli.engine.model

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class LayerTypeTest {

    @Test
    fun `layer types have icon, title and description`() {
        LayerType.entries.forEach { type ->
            Assertions.assertNotNull(type.getIcon(), type.getId())
            Assertions.assertNotNull(type.getTitle(), type.getId())
            Assertions.assertNotNull(type.getDescription(), type.getId())
        }
    }

}