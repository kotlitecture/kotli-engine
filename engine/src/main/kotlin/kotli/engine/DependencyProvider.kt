package kotli.engine

/**
 * Provides dependencies on some type of objects.
 *
 * @param T The type of objects.
 */
interface DependencyProvider<T> {

    /**
     * Returns all dependencies of the given object.
     *
     * @return A list of dependencies.
     */
    fun dependencies(): List<Class<out T>> = emptyList()

}