package org.flowpaint.tools

import _root_.org.flowpaint.brush.Brush
import filters.StrokeListener
import model.Stroke
import util.DataSample

import util.PropertyRegister

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

    if (event.contains(PropertyRegister.LEFT_BUTTON)) {

      val pressed = event.getProperty(PropertyRegister.LEFT_BUTTON, 0) > 0.5f

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
      val brush: Brush = FlowPaintController.currentBrush
      currentStroke = new Stroke( brush )

      val initialSample = new DataSample(currentStatus);

      // TODO: If a tablet is used, this should be initialized to zero, or there should be a filter that waits until pressure and cordinate input has been received.
      initialSample.setProperty( PropertyRegister.PRESSURE, 0.5f )
      initialSample.setProperty( PropertyRegister.RANDOM_SEED, Math.random.toFloat )
      brush.initializeStrokeStart( initialSample )

      addStrokePoint(currentStroke, initialSample )

      // Add stroke, so that we get a preview of it
      // TODO: add with a proper undoable command later
      FlowPaintController.currentPainting.currentLayer.addStroke(currentStroke)

    }

  def addStrokePoint(stroke:Stroke, point:DataSample)
  {
    point.setProperty(PropertyRegister.INDEX, currentPointIndex)
    point.setProperty(PropertyRegister.TIME, (getTime() - currentStrokeStartTime).toFloat / 1000f)

    currentPointIndex += 1

    stroke.brush.processStrokePoint( point, new StrokeListener(){

      def addStrokePoint( pointData : DataSample ) {
        stroke.addPoint( pointData, FlowPaintController.surface )
      }

    })
  }


  def endStroke()
    {
      FlowPaintController.addRecentBrush( currentStroke.brush )

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




