package kotli.engine.utils

import java.net.URL
import java.util.concurrent.ConcurrentHashMap

/**
 * Common utils useful for operations with resources.
 */
object ResourceUtils {

    private val cache = ConcurrentHashMap<URL, String>()

    /**
     * Returns the content of #resourceName if such resource is found in the classloader
     * at the level of #context object (it can be class or an instance).
     *
     * @return resource content as String.
     */
    fun getAsString(context: Any, resourceName: String): String? {
        val url = get(context, resourceName) ?: return null
        return cache.getOrPut(url) { url.readText().trimIndent() }.takeIf { it.isNotEmpty() }
    }

    /**
     * Returns URL of #resourceName if such resource is found in the classloader
     * at the level of #context object (it can be class or an instance).
     *
     * @return resource content as String.
     */
    fun get(context: Any, resourceName: String): URL? {
        return context.javaClass.getResource(resourceName)
    }

}