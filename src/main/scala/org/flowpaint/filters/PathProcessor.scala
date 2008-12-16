package org.flowpaint.filters

import model.Path
import property.Data
import util.{DataSample, Processor}

/**
 *
 *
 * @author Hans Haggstrom
 */

trait PathProcessor extends Processor {

    var firstPoint = true

    override def init(initialSettings : Data) {
      super.init(initialSettings)
      firstPoint = true
    }



    def processPath(path : Path, callback: (Data) => Unit) {
        processPathPoint(pathPointData, callback)
        firstPoint = false
    }


    def handlePathPoint(pathPointData: Data, callback: (Data) => Unit) {
        processPathPoint(pathPointData, callback)
        firstPoint = false
    }

    protected def processPathPoint(pathPointData: Data, callback: (Data) => Unit)


}