package kotli.engine.model

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class FeatureTypeTest {

    @Test
    fun `feature types have icon and title`() {
        FeatureType.entries.forEach { type ->
            Assertions.assertNotNull(type.getIcon(), type.getId())
            Assertions.assertNotNull(type.getTitle(), type.getId())
        }
    }

}