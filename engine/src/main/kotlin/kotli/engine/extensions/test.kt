package kotli.engine.extensions

import kotli.engine.TemplateGenerator
import kotli.engine.model.Feature
import org.jetbrains.annotations.TestOnly

/**
 * Creates list with all features.
 */
@TestOnly
fun TemplateGenerator.getAllFeatures(): List<Feature> {
    return getProviders()
        .map { it.getProcessors() }
        .flatten()
        .map { Feature(id = it.getId()) }
}