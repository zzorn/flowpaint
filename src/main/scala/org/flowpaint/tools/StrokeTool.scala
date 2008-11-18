package org.flowpaint.tools

import filters.StrokeListener
import model.Stroke
import util.DataSample

/**
 *
 *
 * @author Hans Haggstrom
 */

class StrokeTool extends Tool {

  val currentStatus: DataSample = new DataSample()

  var currentStroke: Stroke = null
  var currentPointIndex = 0
  var currentStrokeStartTime = 0L


  def onEvent(event: DataSample) = {

    currentStatus.setValuesFrom(event)

    if (event.contains("leftButton")) {

      val pressed = event.getProperty("leftButton", 0) > 0.5f

      if (pressed != isStrokeActive )
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

    if (isStrokeActive )
      {
        addStrokePoint(currentStroke, event)
      }
  }



  def startStroke()
    {
      currentPointIndex = 0
      currentStrokeStartTime = getTime()
      currentStroke = new Stroke( FlowPaintController.currentBrush )

      val initialSample = new DataSample(currentStatus);

      FlowPaintController.fillDataSampleWithCurrentSettings(initialSample);

      addStrokePoint(currentStroke, initialSample )

      // Add stroke, so that we get a preview of it
      // TODO: add with a proper undoable command later
      FlowPaintController.currentPainting.currentLayer.addStroke(currentStroke)
    }

  def addStrokePoint(stroke:Stroke, point:DataSample)
  {
    point.setProperty("index", currentPointIndex)
    point.setProperty("time", (getTime() - currentStrokeStartTime).toFloat / 1000f)

    currentPointIndex += 1

    stroke.brush.filterStrokePoint( point, new StrokeListener(){

      def addStrokePoint( pointData : DataSample ) {
        stroke.addPoint( pointData, FlowPaintController.surface )
      }

    })
  }


  def endStroke()
    {
      // Temp:
      //      sketchController.currentPainting.currentLayer.addStroke( stroke )

      // TODO: Add command handling system, to allow undo?
      /*
              sketchController.getCommandStack().invoke( new AbstractCommand( "Stroke", true )
              {

                  public void doCommand()
                  {
                      // Allows us to add the stroke in the startStroke method already, and still undo and redo if needed.
                      if ( !group.contains( stroke ) )
                      {
                          group.add( stroke );
                      }
                  }


                  @Override
                  public void undoCommand()
                  {
                      group.remove( stroke );
                  }

              } );
      */

      currentStroke = null;
    }


  def isStrokeActive = currentStroke != null

  def getTime() = System.currentTimeMillis

}




