package kotli.engine

/**
 * Registry interface for accessing template processors.
 */
interface TemplateRegistry {

    /**
     * Retrieves a processor by its ID.
     *
     * @param id The ID of the processor.
     * @return The corresponding processor or null if not found.
     */
    fun get(id: String): TemplateProcessor?

    /**
     * Retrieves all registered processors.
     *
     * @return A list of all available processors.
     */
    fun getAll(): List<TemplateProcessor>
}