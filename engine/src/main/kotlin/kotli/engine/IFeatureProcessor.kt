package kotli.engine

interface IFeatureProcessor : IFeature {

    val id: String

    fun apply(context: TemplateContext) = Unit

    fun remove(context: TemplateContext) = Unit

}