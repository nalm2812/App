package dk.itu.moapd.x9.nalm.ui.list

import dk.itu.moapd.x9.nalm.domain.model.TrafficReportModel

fun interface TrafficReportModelLongClickListener {
    fun onTrafficReportLongClick(trafficReport: TrafficReportModel, position: Int)
}