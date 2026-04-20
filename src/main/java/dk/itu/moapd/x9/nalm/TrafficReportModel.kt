package dk.itu.moapd.x9.nalm

data class TrafficReportModel(
    val title: String,
    val location: String,
    val date: String,
    val reportType: String,
    val severity: String,
    val desc: String,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)