package kotli.engine

import kotli.engine.model.Layer
import kotli.engine.model.LayerTypes
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class DefaultTemplateContextTest {

    @Test
    fun `apply state`() {
        val context = DefaultTemplateContext(
            registry = DefaultTemplateRegistry(),
            layer = Layer(
                id = "",
                name = "",
                namespace = "",
                processorId = TemplateProcessor.App.getId(),
            ),
            parent = null,
            contextPath = "",
        )
        context.onApplyRules("4")
        context.onApplyRules("3")
        context.onApplyRules("2")
        context.onApplyRules("1")
        val rules = context.getRules().groupBy { it.contextPath }.keys
        Assertions.assertEquals(setOf("4", "3", "2", "1"), rules)
    }

}