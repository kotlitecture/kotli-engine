package kotli.engine

import kotli.engine.model.LayerTypes
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class SimpleTemplateRegistryTest {

    @Test
    fun `templated registered in factory`() {
        val templateIds = listOf("1", "2", "3")
        val factory = templateIds
                .map { id ->
                    object : BaseTemplateGenerator() {
                        override fun doPrepare(context: TemplateContext) = Unit
                        override fun createProviders(): List<FeatureProvider> = emptyList()
                        override fun getType(): LayerType = LayerTypes.App
                        override fun getId(): String = id
                    }
                }
                .let { SimpleTemplateRegistry(it) }
        templateIds.forEach { id -> Assertions.assertNotNull(factory.get(id)) }
    }

    @Test
    fun `internal template is not available in getAll`() {
        val factory: TemplateRegistry = SimpleTemplateRegistry(emptyList())
        Assertions.assertSame(TemplateGenerator.App, factory.get(TemplateGenerator.App.getId()))
        Assertions.assertFalse(factory.getAll().contains(TemplateGenerator.App))
    }

}