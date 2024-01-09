package kotli.engine.utils

import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import org.junit.jupiter.api.Assertions
import org.slf4j.LoggerFactory
import java.nio.file.Files
import kotlin.io.path.pathString
import kotlin.test.Test

class PackageUtilsTest {

    private val logger = LoggerFactory.getLogger(PackageUtilsTest::class.java)

    @Test
    fun `rename package`() {
        val oldPackage = "kotli.engine"
        val newPackage = "my.new.app"
        val from = PathUtils.getFromResource("")!!
        val to = Jimfs.newFileSystem(Configuration.unix()).getPath("/")
        PathUtils.copy(from, to)
        val fromFiles = Files.walk(to).toList()
        fromFiles.forEach { file -> logger.debug("file :: {}", file.pathString) }
        PackageUtils.rename(to, oldPackage, newPackage)
        logger.debug("========================")
        val toFiles = Files.walk(to).toList()
        toFiles.forEach { file -> logger.debug("file :: {}", file.pathString) }

        fromFiles.forEachIndexed { index, path ->
            if (path.pathString.startsWith("kotli")) {
                val newFile = toFiles[index]
                Assertions.assertTrue(newFile.pathString.startsWith("my"))
            }
        }
    }

}