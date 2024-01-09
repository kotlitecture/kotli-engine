package kotli.engine

import org.slf4j.LoggerFactory

object TemplateFactory {

    private val logger = LoggerFactory.getLogger(TemplateFactory::class.java)

    private val generators = mutableMapOf<String, ITemplateGenerator>()

    fun register(generator: ITemplateGenerator) {
        logger.debug("register :: {} -> {}", generator.getId(), generator)
        generators[generator.getId()] = generator
    }

    fun get(id: String): ITemplateGenerator {
        return getOrNull(id)!!
    }

    fun getOrNull(id: String): ITemplateGenerator? {
        return generators[id]
    }

    fun getAll(): List<ITemplateGenerator> {
        return generators.values.toList()
    }

}