package kotli.engine.utils

import java.net.URL
import java.util.concurrent.ConcurrentHashMap

object ResourceUtils {

    private val cache = ConcurrentHashMap<URL, String>()

    fun getAsString(context: Any, resourceName: String): String? {
        val url = get(context, resourceName) ?: return null
        return cache.getOrPut(url) { url.readText().trimIndent() }.takeIf { it.isNotEmpty() }
    }

    fun get(context: Any, resourceName: String): URL? {
        return context.javaClass.getResource(resourceName)
    }

}