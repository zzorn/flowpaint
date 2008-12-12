package org.flowpaint.filters

import model.Path
import property.Data
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

trait PathProcessor {
    val settings: Data = new Data

    var firstPoint = true

    def init(settings_ : Data) {
        settings.set(settings_)
        firstPoint = true
        onInit()
    }

    protected def onInit() {}

    def handlePathPoint(pathPointData: Data, callback: (Data) => Unit) {
        processPathPoint(pathPointData, callback)
        firstPoint = false
    }

    protected def processPathPoint(pathPointData: Data, callback: (Data) => Unit)


}