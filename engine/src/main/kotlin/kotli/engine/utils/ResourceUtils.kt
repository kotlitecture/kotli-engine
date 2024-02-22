package kotli.engine.utils

import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * Common utilities useful for operations with resources.
 */
object ResourceUtils {

    private val cache = ConcurrentHashMap<URL, String>()

    /**
     * Returns the content of the resource specified by #resourceName if such a resource is found in the classloader
     * at the level of the #context object (it can be a class or an instance).
     *
     * @param context The context object at the level of which the resource is searched.
     * @param resourceName The name of the resource.
     * @return The resource content as a String.
     */
    fun getAsString(context: Any, resourceName: String): String? {
        val url = get(context, resourceName) ?: return null
        return cache.getOrPut(url) { url.readText().trimIndent() }.takeIf { it.isNotEmpty() }
    }

    /**
     * Returns the URL of the resource specified by #resourceName if such a resource is found in the classloader
     * at the level of the #context object (it can be a class or an instance).
     *
     * @param context The context object at the level of which the resource is searched.
     * @param resourceName The name of the resource.
     * @return The URL of the resource.
     */
    fun get(context: Any, resourceName: String): URL? {
        return context.javaClass.getResource(resourceName)
    }

}