package kotli.engine

import kotli.engine.utils.ResourceUtils
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class FeatureTest : IFeature {

    @Test
    fun `getResourceAsString points to existing file`() {
        Assertions.assertEquals("DOC", ResourceUtils.getAsString(this, "doc.md"))
    }

}