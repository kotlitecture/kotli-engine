package kotli.engine.model

data class Feature(
    val providerId: String,
    val processorId: String,
    val attributes: Map<String, String> = emptyMap()
)