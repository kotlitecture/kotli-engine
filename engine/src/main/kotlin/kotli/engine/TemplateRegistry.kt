package kotli.engine

/**
 * Registry interface for accessing template generators.
 */
interface TemplateRegistry {

    /**
     * Retrieves a generator by its ID.
     *
     * @param id The ID of the generator.
     * @return The corresponding generator or null if not found.
     */
    fun get(id: String): TemplateGenerator?

    /**
     * Retrieves all registered generators.
     *
     * @return A list of all available generators.
     */
    fun getAll(): List<TemplateGenerator>
}