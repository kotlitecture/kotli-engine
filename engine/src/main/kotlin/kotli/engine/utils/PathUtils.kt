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

object PathUtils {

    private val logger = LoggerFactory.getLogger(PathUtils::class.java)

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

    fun isEmptyDir(path: Path): Boolean {
        return path.isDirectory() && Files.list(path).count() == 0L
    }

    fun delete(path: Path) {
        logger.debug("delete :: {}", path)
        path.deleteIfExists()
    }

}