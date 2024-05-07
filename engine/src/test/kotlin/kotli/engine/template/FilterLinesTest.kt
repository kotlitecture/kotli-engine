package kotli.engine.template

import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class FilterLinesTest {

    @Test
    fun `filter lines`() {
        val lines = listOf(
            " ",
            "1",
            "2",
            " ",
            "",
            "3",
            " ",
            " ",
            " ",
            "4",
            " ",
            " ",
        )
        val filtered = lines
            .filterIndexed { index, line ->
                line.isNotBlank() || lines.getOrNull(index - 1)?.isNotBlank() == true
            }
            .dropWhile { it.isBlank() }
            .dropLastWhile { it.isBlank() }
        Assertions.assertEquals(
            listOf(
                "1",
                "2",
                " ",
                "3",
                " ",
                "4"
            ),
            filtered
        )
    }

}