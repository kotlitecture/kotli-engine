@file:OptIn(ExperimentalPathApi::class)

package kotli.engine.utils

import org.slf4j.LoggerFactory
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.copyToRecursively
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.isDirectory
import kotlin.io.path.pathString

/**
 * Common utilities for operations with paths.
 */
object PathUtils {

    private val logger = LoggerFactory.getLogger(PathUtils::class.java)

    /**
     * Returns the path of the #resource if found in the classloader.
     * The #resource is specified relative to the root of the source directory of your code.
     *
     * @param resource The resource path relative to the root of the source directory.
     * @return The path of the resource if found; otherwise, null.
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
     * Copies the contents of the given path #from to the path #to, including all its children.
     *
     * @param from The source path to copy from.
     * @param to The destination path to copy to.
     */
    fun copy(from: Path, to: Path) {
        logger.debug("copy :: {} -> {}", from, to)
        val followLinks = true
        to.createDirectories()
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
     * Checks if the given path is an empty directory and returns true if so.
     *
     * @param path The path to check.
     * @return True if the path is an empty directory; otherwise, false.
     */
    fun isEmptyDir(path: Path): Boolean {
        return path.isDirectory() && Files.walk(path).noneMatch { Files.isRegularFile(it) }
    }

    /**
     * Deletes the given #path if it exists.
     *
     * @param path The path to be deleted.
     */
    fun delete(path: Path) {
        logger.debug("delete :: {}", path)
        runCatching { path.deleteRecursively() }
    }

}