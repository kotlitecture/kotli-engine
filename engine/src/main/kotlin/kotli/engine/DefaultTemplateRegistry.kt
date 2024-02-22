package kotli.engine

/**
 * Simple generator factory implementation providing access to all available generators
 * during the processing of the output structure.
 */
class DefaultTemplateRegistry(private val generators: List<TemplateGenerator>) : TemplateRegistry {

    private val generatorsMap = generators
            .plus(TemplateGenerator.App)
            .associateBy { it.getId() }
            .toMap()

    override fun get(id: String): TemplateGenerator? {
        return generatorsMap[id]
    }

    override fun getAll(): List<TemplateGenerator> {
        return generators.sortedBy { it.getType().getOrder() }
    }

}