package kotli.engine.utils

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.readLines
import kotlin.io.path.writeText

/**
 * Common utils useful for operations with packages.
 */
object PackageUtils {

    /**
     * Renames #oldPackageName to #newPackageName under the given root folder #rootDir.
     * All subsequent files moved to the new package name accordingly.
     */
    fun rename(rootDir: Path, oldPackageName: String, newPackageName: String) {
        val oldPackagePath = oldPackageName.replace('.', '/')
        val newPackagePath = newPackageName.replace('.', '/')

        val oldPackageDir = rootDir.resolve(oldPackagePath)
        val newPackageDir = rootDir.resolve(newPackagePath)

        Files.createDirectories(newPackageDir)
        Files.move(oldPackageDir, newPackageDir, StandardCopyOption.REPLACE_EXISTING)

        Files.walk(rootDir)
            .filter { Files.isRegularFile(it) }
            .forEach { filePath ->
                val fileName = filePath.fileName.toString()
                if (fileName.endsWith(".java") || fileName.endsWith(".kt")) {
                    updatePackageReferences(filePath, oldPackageName, newPackageName)
                }
            }
    }

    private fun updatePackageReferences(
        filePath: Path,
        oldPackageName: String,
        newPackageName: String
    ) {
        val text = filePath
            .readLines()
            .joinToString("\n") {
                it.replace(oldPackageName, newPackageName)
            }
        filePath.writeText(text)
    }

}