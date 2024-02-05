package kotli.engine.model

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class FeatureTypeTest {

    @Test
    fun `feature types have icon and title`() {
        FeatureType.entries.forEach { type ->
            Assertions.assertNotNull(type.getIcon(), type.id)
            Assertions.assertNotNull(type.getTitle(), type.id)
        }
    }

}