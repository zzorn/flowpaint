package org.flowpaint.filters

import org.flowpaint.model.Path
import org.flowpaint.property.Data
import org.flowpaint.util.{DataSample, Configuration}

/**
 *
 *
 * @author Hans Haggstrom
 */

trait PathProcessor extends Configuration {

    var firstPoint = true

    override def init(initialSettings : Data) {
      super.init(initialSettings)
      firstPoint = true
    }

    def isFirstPoint = firstPoint

    def handlePath(pathPointsData: List[Data]) : List[Data] = {
      // NOTE: This can probably be done shorter with some nice list method.
      var result : List[Data] = Nil
      pathPointsData.elements.foreach  ((d : Data) => result = result ::: handlePathPoint( d ))
        

      return result
    }

    def handlePathPoint(pathPointData: Data) : List[Data] = {
        val result = processPathPoint(pathPointData)
        firstPoint = false
        return result
    }

    protected def processPathPoint(pathPointData: Data ) : List[Data]


}


