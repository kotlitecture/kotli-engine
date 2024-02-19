package kotli.engine

/**
 * The simplest version of generator factory, used to access
 * all available generators during processing the output structure.
 */
class SimpleTemplateRegistry(private val generators: List<ITemplateGenerator>) : ITemplateRegistry {

    private val generatorsMap = generators
            .plus(ITemplateGenerator.App)
            .associateBy { it.getId() }

    override fun get(id: String): ITemplateGenerator? {
        return generatorsMap[id]
    }

    override fun getAll(): List<ITemplateGenerator> {
        return generators.sortedBy { it.getType().getOrder() }
    }

}