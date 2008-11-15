package org.flowpaint.input

import jpen._
import jpen.event.PenListener
import model.StrokePoint
import util.DataSample


/**
 *  Recieves pen events, and turns them into data samples.
 *
 * @author Hans Haggstrom
 */
class PenInputHandler extends PenListener {
  private var xOffs = 0f
  private var yOffs = 0f
  private var xScale = 1f
  private var yScale = 1f

  /**Used for syncronizing on the projection variables. */
  private val projectionLock = new Object

  /**
   *  Used to update the projection that should be applied to pen input
   *  (mapping from the screen space to the canvas space)
   */
  def onProjectionChanged(xOffs: Float,
                         yOffs: Float,
                         xScale: Float,
                         yScale: Float) {
    projectionLock.synchronized{
      this.xOffs = xOffs;
      this.yOffs = yOffs;
      this.xScale = xScale;
      this.yScale = yScale;
    }
  }

  def penButtonEvent(event: PButtonEvent): Unit = {

    val dataSample = createDataSample(event)

    val value: Float = if (event.button.value.booleanValue ) 1f else 0f

    event.button.getType() match {
      case PButton.Type.LEFT => dataSample.setProperty("leftButton", value);
      case PButton.Type.RIGHT => dataSample.setProperty("rightButton", value);
      case PButton.Type.CENTER => dataSample.setProperty("centerButton", value);
    }

    sendSample(dataSample);
  }


  def penLevelEvent(event: PLevelEvent): Unit = {

    val dataSample = createDataSample(event)

    def setCoordinate(name: String, value: Float, scale: Float, offset: Float) =
      projectionLock.synchronized{dataSample.setProperty(name, value * scale + offset)}

    event.levels.foreach((level: PLevel) =>
            {
              val value: Float = level.value.floatValue;
              level.getType() match
              {
                case PLevel.Type.X => setCoordinate("x", value, xScale, xOffs)
                case PLevel.Type.Y => setCoordinate("y", value, yScale, yOffs)
                case PLevel.Type.PRESSURE => dataSample.setProperty("pressure", value);
                case PLevel.Type.TILT_X => dataSample.setProperty("tiltX", value);
                case PLevel.Type.TILT_Y => dataSample.setProperty("tiltY", value);
              }
            }
      )

    sendSample(dataSample);
  }

  def penScrollEvent(p1: PScrollEvent): Unit = {
    // Not used currently

  }

  def penTock(p1: Long): Unit = {
    // TODO: If this goes towards zero, it means our listeners are taking up too much time.
    // Reduce amount of messages?
  }

  def penKindEvent(p1: PKindEvent): Unit = {
    // Not used currently

  }

  private def sendSample( sample : DataSample ) {
    // TODO
  }

  private def createDataSample(event: jpen.PenEvent): DataSample = {
    val dataSample = new DataSample()
    dataSample.setProperty("time", event.getTime().toFloat / 1000.0f)
    dataSample
  }


}
