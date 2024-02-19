package kotli.engine

import kotli.engine.model.LayerType
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class SimpleTemplateRegistryTest {

    @Test
    fun `templated registered in factory`() {
        val templateIds = listOf("1", "2", "3")
        val factory = templateIds
                .map { id ->
                    object : AbstractTemplateGenerator() {
                        override fun doPrepare(context: TemplateContext) = Unit
                        override fun createProviders(): List<IFeatureProvider> = emptyList()
                        override fun getType(): ILayerType = LayerType.App
                        override fun getId(): String = id
                    }
                }
                .let { SimpleTemplateRegistry(it) }
        templateIds.forEach { id -> Assertions.assertNotNull(factory.get(id)) }
    }

    @Test
    fun `internal template is not available in getAvailable`() {
        val factory: ITemplateRegistry = SimpleTemplateRegistry(emptyList())
        Assertions.assertSame(ITemplateGenerator.App, factory.get(ITemplateGenerator.App.getId()))
        Assertions.assertFalse(factory.getAll().contains(ITemplateGenerator.App))
    }

}