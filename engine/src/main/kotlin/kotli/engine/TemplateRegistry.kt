package kotli.engine

interface TemplateRegistry {

    /**
     * Finds generator by its id.
     *
     * @param id - generator id.
     * @return generator with given id or null.
     */
    fun get(id: String): TemplateGenerator?

    /**
     * Finds all registered generators.
     */
    fun getAll(): List<TemplateGenerator>
}