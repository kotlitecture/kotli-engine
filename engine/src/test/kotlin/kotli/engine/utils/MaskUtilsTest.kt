package kotli.engine.utils

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class MaskUtilsTest {

    @Test
    fun `is mask`() {
        Assertions.assertTrue(MaskUtils.isMask("*.txt"))
        Assertions.assertTrue(MaskUtils.isMask("?.txt"))
    }

    @Test
    fun `is not mask`() {
        Assertions.assertFalse(MaskUtils.isMask("txt.txt"))
    }

    @Test
    fun `mask matches given path`() {
        Assertions.assertTrue(MaskUtils.toRegex("*.txt").matches("aaa/bbb.txt"))
        Assertions.assertTrue(MaskUtils.toRegex("*.txt").matches("aaa.txt"))
        Assertions.assertFalse(MaskUtils.toRegex("*.txt").matches("aaa.tx"))
        Assertions.assertTrue(MaskUtils.toRegex("*.tx*").matches("aaa.tx"))
        Assertions.assertFalse(MaskUtils.toRegex("*.tx?").matches("aaa.tx"))
        Assertions.assertTrue(MaskUtils.toRegex("***.*tx***").matches("aaa.tx"))
    }

}