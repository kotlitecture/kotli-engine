package kotli.engine

/**
 * Simple processor factory implementation providing access to all available processors
 * during the processing of the output structure.
 */
class DefaultTemplateRegistry(vararg processors: TemplateProcessor) : TemplateRegistry {

    private val processorList = processors.toList().sortedBy { it.getType().getOrder() }

    private val processorsMap = processorList
        .plus(TemplateProcessor.App)
        .associateBy { it.getId() }
        .toMap()

    override fun get(id: String): TemplateProcessor? = processorsMap[id]

    override fun getAll(): List<TemplateProcessor> = processorList

}