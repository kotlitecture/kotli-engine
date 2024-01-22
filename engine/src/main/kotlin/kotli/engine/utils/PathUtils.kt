@file:OptIn(ExperimentalPathApi::class)

package kotli.engine.utils

import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.deleteIfExists
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

/**
 * Common utils useful for operations with paths.
 */
object PathUtils {

    private val logger = LoggerFactory.getLogger(PathUtils::class.java)

    /**
     * Returns path of the #resource if such is found in the classloader.
     * The #resource is passed relative to the root of the source directory of your code.
     */
    fun getFromResource(resource: String): Path? {
        val fromUri = javaClass.getResource("/$resource")?.toURI() ?: return null
        val path = runCatching { Path.of(fromUri) }
            .onFailure { logger.error("getFromResource", it) }
            .getOrNull()
            ?: run {
                val fs = FileSystems.newFileSystem(fromUri, emptyMap<String, String>())
                fs.getPath(resource)
            }
        logger.debug("getFromResource :: {} -> {}", fromUri, path)
        return path
    }

    /**
     * Copies given path #from to the path #to with all its children.
     */
    fun copy(from: Path, to: Path) {
        logger.debug("copy :: {} -> {}", from, to)
        val followLinks = true
        from.copyToRecursively(
            to,
            followLinks = followLinks,
            copyAction = { src, dst ->
                val toDst = dst.resolve(dst.pathString.replace("\\", "/"))
                src.copyToIgnoringExistingDirectory(toDst, followLinks)
            }
        )
    }

    /**
     * Checks if the given path is empty directory and returns true if so.
     */
    fun isEmptyDir(path: Path): Boolean {
        return path.isDirectory() && Files.list(path).count() == 0L
    }

    /**
     * Deletes given #path if such exists.
     */
    fun delete(path: Path) {
        logger.debug("delete :: {}", path)
        path.deleteIfExists()
    }

}