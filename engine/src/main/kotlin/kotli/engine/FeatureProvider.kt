package kotli.engine

abstract class FeatureProvider : IFeatureProvider {

    private val all by lazy { createProcessors() }
    private val byId by lazy { all.associateBy { it.id } }

    override fun getProcessors(): List<IFeatureProcessor> = all
    override fun getProcessor(id: String): IFeatureProcessor = byId[id]!!

    abstract fun createProcessors(): List<IFeatureProcessor>

}