package kotli.engine

/**
 * The simplest version of generator factory, used to access
 * all available generators during processing the output structure.
 */
class SimpleTemplateRegistry(private val generators: List<TemplateGenerator>) : TemplateRegistry {

    private val generatorsMap = generators
            .plus(TemplateGenerator.App)
            .associateBy { it.getId() }

    override fun get(id: String): TemplateGenerator? {
        return generatorsMap[id]
    }

    override fun getAll(): List<TemplateGenerator> {
        return generators.sortedBy { it.getType().getOrder() }
    }

}