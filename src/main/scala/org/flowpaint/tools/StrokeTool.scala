package org.flowpaint.tools

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
        currentStroke.addPoint(event, controller.surface);
      }
  }

  def startStroke(sketchController: FlowPaintController)
    {

      println("stroke started")

      currentStroke = new Stroke( sketchController.currentBrush )

      val initialSample = new DataSample(currentStatus);

      sketchController.fillDataSampleWithCurrentSettings(initialSample);

      currentStroke.addPoint(initialSample);

      // Add stroke, so that we get a preview of it
      // TODO: add with a proper undoable command later
      sketchController.currentPainting.currentLayer.addStroke(currentStroke)
    }


  def endStroke(sketchController: FlowPaintController)
    {
      println("stroke ended")

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

}




