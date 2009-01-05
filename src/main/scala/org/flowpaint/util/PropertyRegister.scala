package org.flowpaint.util

import scala.collection.mutable.HashMap
import scala.collection.{mutable, immutable}

/**
 *  Keep track of the registered stroke properties.  Provides a name to id mapping service.
 *
 * @author Hans Haggstrom
 */
// TODO: Add synchronization, as this is accessed from the pen listeners
object PropertyRegister {

  private val idToName = new HashMap[Int, String]()
  private val nameToId = new HashMap[String, Int]()

  // Common constants
  val RED = getId("red")
  val GREEN = getId("green")
  val BLUE = getId("blue")
  val ALPHA = getId("alpha")
  val PATH_X = getId("pathX")
  val PATH_Y = getId("pathY")
  val LEFT_EDGE_X = getId("leftEdgeX")
  val LEFT_EDGE_Y = getId("leftEdgeY")
  val RIGHT_EDGE_X = getId("rightEdgeX")
  val RIGHT_EDGE_Y = getId("rightEdgeY")
  val SCREEN_X = getId("screenX")
  val SCREEN_Y = getId("screenY")
  val CANVAS_X = getId("canvasX")
  val CANVAS_Y = getId("canvasY")
  val ANGLE = getId("angle")
  val SATURATION = getId("saturation")
  val HUE = getId("hue")
  val LIGHTNESS = getId("lightness")
  val SATURATION_DELTA = getId("saturationDelta")
  val HUE_DELTA = getId("hueDelta")
  val LIGHTNESS_DELTA = getId("lightnessDelta")
  val VALUE = getId("value")
  val PRESSURE = getId("pressure")
  val POSTION_ALONG_STROKE = getId("positionAlongStroke")
  val POSITION_ACROSS_STROKE = getId("positionAcrossStroke")
  val RANDOM_SEED= getId("randomSeed")
  val INDEX = getId("index")
  val TIME = getId("time")
  val LEFT_BUTTON = getId("leftButton")
  val RIGHT_BUTTON = getId("rightButton")
  val CENTER_BUTTON = getId("centerButton")
  val TILT_X = getId("tiltX")
  val TILT_Y = getId("tiltY")
  val RADIUS = getId("radius")
  val MAX_RADIUS = getId("maxRadius")
  val PREVIOUS_SEGMENT_LENGTH = getId("previousSegmentLength")
  val DISTANCE= getId("distance")



  /**
   * @param name the name to get or create an id for.
   *
   * @return the id for the specified variable name.  If none is registered before, a new one is registered
   *          and returned.
   */
  def getId(name: String): Int = {

    nameToId.get(name) match {

      case Some(id) => id

      case None => {
        val id = idToName.size

        idToName.put(id, name)
        nameToId.put(name, id)

        id
      }
    }
  }


  /**
   * @param id the id to get a name for.
   *
   * @return the name of the variable with the specified id, or null if none registered.
   */
  def getName(id: Int): String = idToName(id)


  /**
   *  Clears the registry from all registered id:s.  Used in unit testing.
   */
  def clear = {
    idToName.clear
    nameToId.clear
  }


}