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


  def onEvent(event: DataSample, controller: FlowPaintController) = {

    currentStatus.setValuesFrom(event)

    if (event.contains("leftButton")) {

      val pressed = event.getProperty("leftButton", 0) > 0.5f

      if (pressed != isStrokeActive )
        {
          if (pressed)
            {
              startStroke(controller)
            }
          else
            {
              endStroke(controller)
            }
        }
    }

    if (isStrokeActive )
      {
        addStrokePoint(currentStroke, event, controller)
      }
  }



  def startStroke(sketchController: FlowPaintController)
    {
      currentPointIndex = 0
      currentStrokeStartTime = getTime()
      currentStroke = new Stroke( sketchController.currentBrush )

      val initialSample = new DataSample(currentStatus);

      sketchController.fillDataSampleWithCurrentSettings(initialSample);

      addStrokePoint(currentStroke, initialSample, sketchController)

      // Add stroke, so that we get a preview of it
      // TODO: add with a proper undoable command later
      sketchController.currentPainting.currentLayer.addStroke(currentStroke)
    }

  def addStrokePoint(stroke:Stroke, point:DataSample, controller: FlowPaintController)
  {
    point.setProperty("index", currentPointIndex)
    point.setProperty("time", (getTime() - currentStrokeStartTime).toFloat / 1000f)
    
    stroke.brush.filterStrokePoint( point, new StrokeListener(){

      def addStrokePoint( pointData : DataSample ) {
        stroke.addPoint( pointData, controller.surface )
      }

    })
  }


  def endStroke(sketchController: FlowPaintController)
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




