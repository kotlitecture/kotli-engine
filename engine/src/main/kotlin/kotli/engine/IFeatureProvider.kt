package kotli.engine

import kotli.engine.model.FeatureType

interface IFeatureProvider : IFeature {

    val id: String

    val type:FeatureType

    fun isRequired(): Boolean = false

    fun isMultiple(): Boolean = false

    fun getProcessors(): List<IFeatureProcessor>

    fun getProcessor(id: String): IFeatureProcessor

}