package kotli.engine

/**
 * Provides dependencies on some type of objects.
 */
interface DependencyProvider<T> {

    /**
     * Returns all dependencies of given object.
     */
    fun dependencies(): List<Class<out T>> = emptyList()

}