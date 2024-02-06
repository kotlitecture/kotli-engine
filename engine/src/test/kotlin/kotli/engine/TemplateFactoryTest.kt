package kotli.engine

import kotli.engine.model.LayerType
import org.junit.jupiter.api.Assertions
import kotlin.test.Test

class TemplateFactoryTest {

    @Test
    fun `templated registered in factory`() {
        val templateIds = listOf("1", "2", "3")
        templateIds.forEach { id ->
            object : AbstractTemplateGenerator() {
                override fun doPrepare(context: TemplateContext) = Unit
                override fun createProviders(): List<IFeatureProvider> = emptyList()
                override fun getType(): ILayerType = LayerType.App
                override fun getId(): String = id
            }
        }
        templateIds.forEach { id ->
            Assertions.assertNotNull(TemplateFactory.get(id))
        }
    }

}