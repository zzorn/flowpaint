package org.flowpaint.ui

import brush.Brush
import filters.StrokeListener
import ink.Ink
import java.awt.event.{ComponentListener, MouseAdapter}
import java.awt.{Graphics2D, Dimension, Graphics, Color}
import javax.swing.JPanel
import model.{Stroke, Painting, Path}
import property.{Data, DataImpl}
import renderer.SingleRenderSurface
import util.DataSample

object BrushPreview {
  def brushPreviewStrokeGenerator(f:Float, w:Float, h:Float, data:Data) {

     val pressure = 0.5f + 0.5f*Math.cos( 2*Math.Pi * f + Math.Pi ).toFloat

     data.setFloatProperty("x", w * f)
     data.setFloatProperty("y", h * f)
     data.setFloatProperty("pressure", pressure)
     data.setFloatProperty("time", f * 0.5f)
   }

}


/**
 * A JComponent that renders a view of the specified stroke points with the specified brush.
 *
 * @author Hans Haggstrom
 */
class BrushPreview(val brush: Brush,
                  val strokePointCalculator : ( Float, Float, Float, Data ) => Unit ,
                  val overlayPainter : (Graphics2D) => Unit ) extends JPanel {

  val SIZE = 32


  def this( brush_ : Brush ) {
    this( brush_, BrushPreview.brushPreviewStrokeGenerator, null )
  }

  if (brush== null) throw new IllegalArgumentException("brush should not be null")
  if (strokePointCalculator == null) throw new IllegalArgumentException("strokePointCalculator should not be null")

  private val stroke = new Stroke(brush)
  private val painting = new Painting()
  private val surface = new SingleRenderSurface(painting)
  private val paintPanel = new PaintPanel(surface, false)

  setLayout(new java.awt.BorderLayout())
  setPreferredSize(new Dimension(SIZE, SIZE))
  setMinimumSize(new Dimension(8, 8))
  setMaximumSize(new Dimension(10000, 10000))
  setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.BLACK, 1))

  painting.currentLayer.addStroke(stroke)

  add(paintPanel, java.awt.BorderLayout.CENTER)

  update()

  addComponentListener(new ComponentListener() {
    def componentMoved(e: java.awt.event.ComponentEvent) {}
    def componentShown(e: java.awt.event.ComponentEvent) {}
    def componentHidden(e: java.awt.event.ComponentEvent) {}
    def componentResized(e: java.awt.event.ComponentEvent) {update()}
  })

  // Update the stroke when the brush changes
  brush.addChangeListener( { b => update() } )


  
  override def paintChildren(g: Graphics): Unit = {
    super.paintChildren( g )

    if (overlayPainter  != null) overlayPainter( g.asInstanceOf[Graphics2D] )
  }

  /**
   * Call this if the preview stroke should be re-rendered.
   */
  def update() {

    stroke.clear()

    val w = getWidth().toFloat
    val h = getHeight().toFloat

    val STEPS = 10

    val path : Path = stroke.addPath( brush )

    path.addPoint( new DataImpl( brush.settings ) )

    def generatePoint(i: Int) {
      val f: Float = (1f * i) / (1f * STEPS)


      val dataSample = new DataImpl()
      dataSample.setFloatProperty("index", i.toFloat)
      dataSample.setFloatProperty("randomSeed", 1231424)

      strokePointCalculator( f, w, h, dataSample )

      path.addPoint( dataSample )

    }

    //generatePoint(-1) // NOTE: Generating one initial point, as some of the filters remove initial points.  Fix filters..
    for (i <- 0 to STEPS + 1) {
      generatePoint(i)
    }

    surface.updateSurface()

    repaint()
  }

  def setBackgroundColor( color :Color ) {
    painting.backgroundColor = color

    repaint()
  }




}