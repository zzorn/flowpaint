package org.flowpaint.renderer.packedsurface


import java.awt.event._
import java.awt.{Graphics2D, Color, Graphics}
import javax.swing.JPanel
import org.flowpaint.util.{ColorUtils, RightMouse, DragListener, MouseEventMixin}

/**
 *
 *
 * @author Hans Haggstrom
 */
class VisibleSurface (renderer : (Float, Float) => Float) extends JPanel with MouseEventMixin {

  var panX = 0f
  var panY = 0f
  var scale = 1f


  //raster.addChangeListener( () => repaint() )

  addDragListener( RightMouse, new DragListener[Object] {
    override def drag(startX: Int, startY: Int, prevX: Int, prevY: Int, x: Int, y: Int, e: MouseEvent, draggedObject: Object) {
      panX += (x - prevX)
      panY += (y - prevY)
      repaint()
    }
  } )

  override def mouseWheelRolled(roll: Int, e: MouseEvent) {
    scale = 0.1f max scale - roll * 0.1f
    repaint()
  }

  override def paintComponent(g: Graphics) {
    g.setColor( Color.WHITE )
    g.clearRect( 0, 0, getWidth, getHeight )

    var y = 0
    while( y < getHeight ) {

      var x = 0
      while( x < getWidth) {
        val value = renderer(
          (x - panX - getWidth / 2) * scale ,
          (y - panY - getHeight / 2) * scale )
        g.setColor( new Color( ColorUtils.createRGBAColor( value, value, value, 1f ) ) )
        g.drawLine( x, y, x, y )
        x +=1
      }
      y += 1
    }

/*
    raster.render(
      g.asInstanceOf[Graphics2D],
      getWidth() / 2f + panX,
      getHeight() / 2f + panY,
      scale )
*/
  }

  def projectX( screenX : Int ) : Float = projectCoord( screenX, getWidth,  panX )
  def projectY( screenY : Int ) : Float = projectCoord( screenY, getHeight, panY )

  def projectCoord ( c : Int, size : Int, pan : Float ) : Float = (c.toFloat - size.toFloat / 2f - pan) / scale

}


