package org.flowpaint.tools

import org.flowpaint.brush.Brush
import org.flowpaint.property.{Data, DataImpl}
import org.flowpaint.model.Stroke

import org.flowpaint.util.{PropertyRegister, DataSample}
import org.flowpaint.FlowPaintController

/**
 *
 *
 * @author Hans Haggstrom
 */

class StrokeTool extends Tool {
  val currentStatus: Data = new DataImpl()

  var currentStroke: Stroke = null
  var currentPointIndex = 0
  var currentStrokeStartTime = 0L

  private var mouseDetected = false
  private var mouseDetectionCounter = 0
  private val MouseDetectionThreshold = 10 // After how many datapoints without pen features do we assume a mouse?


  private def detectMouse(event: DataSample) {
    // Temporary workaround solution, in the end would be best to get the information from JPen.

    def isFractionalNumber(n: Float) = Math.abs(n - Math.rint(n)) >= 0.0001f

    val pressure = event.getProperty(PropertyRegister.PRESSURE, 0)
    val x = event.getProperty(PropertyRegister.PATH_X, 0)
    val y = event.getProperty(PropertyRegister.PATH_Y, 0)

    // A mouse will not generate fractional coordinates or pressure, so we can detect it by assuming a
    // mouse is used if no fractions are detected in these variables for some number of stroke points.
    if (isFractionalNumber(x) || isFractionalNumber(y) || isFractionalNumber(pressure)) {
      mouseDetectionCounter = 0
      mouseDetected = false
    }
    else mouseDetectionCounter += 1

    if (mouseDetectionCounter > MouseDetectionThreshold) mouseDetected = true
  }


  def onEvent(event: DataSample) = {

    currentStatus.setFloatProperties(event)

    detectMouse(event)

    if (event.contains(PropertyRegister.LEFT_BUTTON)) {

      val pressed = event.getProperty(PropertyRegister.LEFT_BUTTON, 0) > 0.5f

      if (pressed != isStrokeActive)
        {
          if (pressed)
            {
              startStroke()
            }
          else
            {
              endStroke()
            }
        }
    }

    if (isStrokeActive)
      {
        val data = new DataImpl()
        data.setFloatProperties(event)

        addStrokePoint(currentStroke, data)
      }
  }


  def startStroke()
    {
      // Skip if we dont have a brush
      if (FlowPaintController.currentBrush == null) return

      // Take a snapshot of the state.
      FlowPaintController.surface.undoSnapshot()

      currentPointIndex = 0
      currentStrokeStartTime = getTime()
      val brush: Brush = FlowPaintController.currentBrush.createCopy
      currentStroke = new Stroke(brush)

      val initialSample = new DataImpl(brush.settings);
      initialSample.setValuesFrom(currentStatus)

      // TODO: If a tablet is used, this should be initialized to zero,
      // or there should be a filter that waits until pressure and cordinate input has been received.
      val initialPressure = if (mouseDetected) 1f else 0f
      initialSample.setFloatProperty(PropertyRegister.PRESSURE, initialPressure)
      initialSample.setFloatProperty(PropertyRegister.RANDOM_SEED, Math.random.toFloat)

      addStrokePoint(currentStroke, initialSample)

      FlowPaintController.addRecentBrush(currentStroke.brush)
    }


  def addStrokePoint(stroke: Stroke, point: Data)
    {


      if (stroke != null) {
        point.setFloatProperty(PropertyRegister.INDEX, currentPointIndex)
        point.setFloatProperty(PropertyRegister.TIME, (getTime() - currentStrokeStartTime).toFloat / 1000f)

        currentPointIndex += 1

        stroke.addInputPoint(point, FlowPaintController.surface)

      }
    }


  def endStroke()
    {
      // Skip if we dont have a  stroke
      if (currentStroke == null) return

      // Add command handling system, to allow undo
      FlowPaintController.storeStroke(currentStroke)

      currentStroke = null;
    }


  def isStrokeActive = currentStroke != null

  def getTime() = System.currentTimeMillis

}




