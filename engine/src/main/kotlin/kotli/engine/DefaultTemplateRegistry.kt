package kotli.engine

/**
 * Simple processor factory implementation providing access to all available processors
 * during the processing of the output structure.
 */
class DefaultTemplateRegistry(private val processors: List<TemplateProcessor>) : TemplateRegistry {

    private val processorsMap = processors
            .plus(TemplateProcessor.App)
            .associateBy { it.getId() }
            .toMap()

    override fun get(id: String): TemplateProcessor? {
        return processorsMap[id]
    }

    override fun getAll(): List<TemplateProcessor> {
        return processors.sortedBy { it.getType().getOrder() }
    }

}