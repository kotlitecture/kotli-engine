package kotli.engine

import kotli.engine.model.LayerTypes
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class DefaultTemplateRegistryTest {

    @Test
    fun `templated registered in factory`() {
        val templateIds = listOf("1", "2", "3")
        val registry = templateIds
            .map { id ->
                object : BaseTemplateProcessor() {
                    override fun doPrepare(state: TemplateState) = Unit
                    override fun createProviders(): List<FeatureProvider> = emptyList()
                    override fun getType(): LayerType = LayerTypes.App
                    override fun getId(): String = id
                }
            }
            .let { DefaultTemplateRegistry(*it.toTypedArray()) }
        templateIds.forEach { id -> Assertions.assertNotNull(registry.get(id)) }
    }

    @Test
    fun `internal template is not available in getAll`() {
        val registry: TemplateRegistry = DefaultTemplateRegistry()
        Assertions.assertSame(TemplateProcessor.App, registry.get(TemplateProcessor.App.getId()))
        Assertions.assertFalse(registry.getAll().contains(TemplateProcessor.App))
    }

}