package org.flowpaint.tools

import _root_.org.flowpaint.brush.Brush
import _root_.org.flowpaint.property.{Data, DataImpl}
import filters.StrokeListener
import javax.swing.SwingUtilities
import model.Stroke
import util.DataSample

import util.PropertyRegister

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


    def onEvent(event: DataSample) = {

        currentStatus.setFloatProperties(event)

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
              data.setFloatProperties( event )

                addStrokePoint(currentStroke, data)
            }
    }


    def startStroke()
        {
            // Skip if we dont have a brush
            if (FlowPaintController.currentBrush == null) return

            currentPointIndex = 0
            currentStrokeStartTime = getTime()
            val brush: Brush = FlowPaintController.currentBrush.createCopy
            currentStroke = new Stroke(brush)

            val initialSample = new DataImpl(brush.settings );
            initialSample.setValuesFrom( currentStatus )

            // TODO: If a tablet is used, this should be initialized to zero,
            // or there should be a filter that waits until pressure and cordinate input has been received.
            //initialSample.setFloatProperty(PropertyRegister.PRESSURE, 0.5f)
            initialSample.setFloatProperty(PropertyRegister.RANDOM_SEED, Math.random.toFloat)

            addStrokePoint(currentStroke, initialSample)

            // Add stroke, so that we get a preview of it
            // TODO: add with a proper undoable command later
            FlowPaintController.currentPainting.currentLayer.addStroke(currentStroke)

            FlowPaintController.addRecentBrush( currentStroke.brush )
        }

    def addStrokePoint(stroke: Stroke, point: Data)
        {
            if (stroke != null) {
                point.setFloatProperty(PropertyRegister.INDEX, currentPointIndex)
                point.setFloatProperty(PropertyRegister.TIME, (getTime() - currentStrokeStartTime).toFloat / 1000f)

                currentPointIndex += 1

                stroke.addInputPoint(point, FlowPaintController.surface )

            }
        }


    def endStroke()
        {
            // Skip if we dont have a  stroke
            if (currentStroke == null) return

            // Temp:
            //      sketchController.currentPainting.currentLayer.addStroke( stroke )

            // TODO: Add command handling system, to allow undo
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




