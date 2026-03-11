package dk.itu.moapd.x9.nalm

data class TrafficReport(
    val title: String,
    val location: String,
    val date: String,
    val reportType: String,
    val severity: String,
    val desc: String
)