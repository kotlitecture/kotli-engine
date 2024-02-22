package kotli.engine

/**
 * FeatureType is a logical group for features.
 * It is intended to be used only on the frontend to highlight similarity
 * or common aspects of some of the features.
 */
interface FeatureType : Dictionary {

    /**
     * Indicates whether the type is marked as internal. If true, it will not be available through public config (UI).
     * It can be useful when you need to declare some type which is common for several others,
     * but is useless from the user perspective and should not be available to choose.
     */
    fun isInternal(): Boolean = false

}