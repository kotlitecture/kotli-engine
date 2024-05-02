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
        Assertions.assertFalse(MaskUtils.toRegex("*/aaa.txt").matches("aaa.txt"))
        Assertions.assertTrue(MaskUtils.toRegex("*/aaa.txt").matches("bb/aaa.txt"))
        Assertions.assertFalse(MaskUtils.toRegex("*\\aaa.txt").matches("aaa.txt"))
        Assertions.assertTrue(MaskUtils.toRegex("*\\aaa.txt").matches("/dd/bb/aaa.txt"))
    }

    @Test
    fun `check mask`() {
        val path = "/Users/composeApp/src/commonMain/kotlin/kotli/app/showcases/datasource/keyvalue/object/ObjectKeyValueScreen.kt"
        val mask = "composeApp/src/*.kt"
        val regexp = MaskUtils.toRegex(mask)
        println("regexp :: ${regexp.pattern}")
        Assertions.assertTrue(regexp.matches(path))
    }

}