package org.flowpaint

import brush.GradientTestBrush
import java.awt.Dimension
import javax.swing.JFrame
import model.{Stroke, Painting, StrokePoint}
import renderer.SingleRenderSurface

/**
 * Main entrypoint for FlowPaint.
 *
 */
object FlowPaint {

  val sizeX = 1000
  val sizeY = 700

  def main(args: Array[String])  {
    
    println( "FlowPaint started." )

    // Init model
    val painting = createTestPainting()

    // Init picture buffer
    val surface = new SingleRenderSurface( painting )

    // Init UI
    val paintPanel = new PaintPanel( surface )

    // Create UI Frame
    val frame = new JFrame()
    frame.setContentPane(paintPanel)
    paintPanel.setPreferredSize(new Dimension(sizeX, sizeY))
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.pack
    frame.show
  }

  private def createTestPainting() : Painting = {

    val brush = new GradientTestBrush()
    
    val stroke = new Stroke( brush )
    def createPoint(x : Float, y:Float, angle:Float, radius:Float):StrokePoint = {
      val strokePoint = new StrokePoint
      strokePoint.setProperty( "x",x )
      strokePoint.setProperty( "y",y )
      strokePoint.setProperty( "angle",Math.toRadians(angle).toFloat)
      strokePoint.setProperty( "radius",radius )
      strokePoint
    }
    stroke.points.add( createPoint( 100, 100, 190, 20 ) )
    stroke.points.add( createPoint( 200, 150, 163, 40 ) )
    stroke.points.add( createPoint( 300, 300, 144, 50 ) )
    stroke.points.add( createPoint( 400, 400, 122, 70 ) )
    stroke.points.add( createPoint( 700, 500, 90, 100 ) )

    val painting = new Painting()
    painting.currentLayer.addStroke( stroke )

    painting
  }

}
