package org.flowpaint

import java.awt.Dimension
import javax.swing.JFrame
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.DataSample

/**
 * Main entrypoint for FlowPaint.
 *
 */
object FlowPaint {

  val sizeX = 1000
  val sizeY = 700


  def main(args: Array[String])  {
    
    println( "FlowPaint started." )

    // Init controller
    val controller = new FlowPaintController()

    // Create UI Frame
    val frame = new JFrame()
    frame.setContentPane(controller.paintPanel)
    controller.paintPanel.setPreferredSize(new Dimension(sizeX, sizeY))
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.pack
    frame.show
  }

  
/*
  private def createTestPainting() : Painting = {

    val brush = new Brush()
    
    val stroke = new Stroke( brush )
    def createPoint(x : Float, y:Float, angle:Float, radius:Float):DataSample = {
      val strokePoint = new DataSample
      strokePoint.setProperty( "x",x )
      strokePoint.setProperty( "y",y )
      strokePoint.setProperty( "angle",Math.toRadians(angle).toFloat)
      strokePoint.setProperty( "radius",radius )
      strokePoint
    }
    stroke.addPoint( createPoint( 100, 100, 190, 20 ) )
    stroke.addPoint( createPoint( 200, 150, 163, 40 ) )
    stroke.addPoint( createPoint( 300, 300, 144, 50 ) )
    stroke.addPoint( createPoint( 400, 400, 122, 70 ) )
    stroke.addPoint( createPoint( 700, 500, 90, 100 ) )

    val painting = new Painting()
    painting.currentLayer.addStroke( stroke )

    painting
  }
*/

}
