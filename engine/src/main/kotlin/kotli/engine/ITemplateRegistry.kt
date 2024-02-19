package kotli.engine

interface ITemplateRegistry {

    /**
     * Finds generator by its id.
     *
     * @param id - generator id.
     * @return generator with given id or null.
     */
    fun get(id: String): ITemplateGenerator?

    /**
     * Finds all registered generators.
     */
    fun getAll(): List<ITemplateGenerator>
}