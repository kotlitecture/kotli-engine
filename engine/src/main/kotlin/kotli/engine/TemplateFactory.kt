package kotli.engine

import org.slf4j.LoggerFactory

/**
 * All template generators must be self-registered in this factory through #register(generator) method.
 */
object TemplateFactory {

    private val logger = LoggerFactory.getLogger(TemplateFactory::class.java)

    private val generators = mutableMapOf<String, ITemplateGenerator>()

    /**
     * Registers given #generator for use by other components.
     *
     * @param generator - generator to register.
     */
    fun register(generator: ITemplateGenerator) {
        logger.debug("register :: {} -> {}", generator.getId(), generator)
        generators[generator.getId()] = generator
    }

    /**
     * Finds generator by its id.
     *
     * @param id - generator id.
     * @throws NullPointerException if generator not found.
     */
    fun get(id: String): ITemplateGenerator {
        return getOrNull(id)!!
    }

    /**
     * Finds generator by its id.
     *
     * @param id - generator id
     *
     * @return generator or null.
     */
    fun getOrNull(id: String): ITemplateGenerator? {
        return generators[id]
    }

    /**
     * Finds all registered generators.
     */
    fun getAll(): List<ITemplateGenerator> {
        return generators.values.toList()
    }

}