package org.flowpaint.input

import java.awt.event._
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue}
import jpen._
import jpen.event.PenListener
import org.flowpaint.util.DataSample
import org.flowpaint.util.PerformanceTester.time
import org.flowpaint.util.PropertyRegister

/**
 *  Recieves pen events, and turns them into data samples, that are sent to the specified sampleListener.
 *
 *  Also has capability to listen to mouse input, although it is not currently used.
 *
 * @author Hans Haggstrom
 */
// NOTE: Commented out code contains stuff for listening to events in one thread,
// and sending batches of them to swing at once.  This was not needed however, as the swing side seems to be
// fast enough not to block the event retrieval, and JPen maybe already does something similar.
class InputHandler(sampleListener: (DataSample) => Unit) extends PenListener with MouseMotionListener with MouseListener {
  private var xOffs = 0f
  private var yOffs = 0f
  private var xScale = 1f
  private var yScale = 1f

  /**Used for syncronizing on the projection variables. */
  /*
    private val projectionLock = new Object
  */

  private val MAX_INPUT_EVENT_QUEUE = 10000
  private val MAX_SAMPLES_TO_HANDLE_AT_ONCE = 100
  private val myQueuedInput: BlockingQueue[DataSample] = new ArrayBlockingQueue[DataSample](MAX_INPUT_EVENT_QUEUE);

  /*
    // Handle DataSamples in the queue with tools that run in the swing thread
    val sampleMover : Thread = new Thread( new Runnable()
    {

        def run() {
            val samples = new ArrayList[DataSample]()

            while ( true )
            {
                // Get next samples
                waitForSamples( samples )

                // Handle the samples in the Swing thread.  Blocks until ready
                sendSamplesInSwingThread( samples )

                // Reuse collection.
                samples.clear();
            }
        }

    } )
  */


  /*
    sampleMover.start();
  */


  /*
    def waitForSamples( samples : ArrayList[DataSample] )
    {
        // Wait for first sample
        var dataSample : DataSample = null
        try
        {
            dataSample = myQueuedInput.take()
        }
        catch
        {
          case e : InterruptedException => // We were interrupted, didn't get anything.
        }

        if ( dataSample != null )
        {
            samples.add( dataSample );
        }

        // Get the rest of the available samples
        myQueuedInput.drainTo( samples, MAX_SAMPLES_TO_HANDLE_AT_ONCE );
    }
  */

  /*
    def sendSamplesInSwingThread( samples : ArrayList[DataSample])
    {
        try
        {
            javax.swing.SwingUtilities.invokeAndWait( new Runnable()
            {

                def run()
                {
                  for( i <- 0 until samples.size) sampleListener( samples.get(i) )
                }

            } );
        }
        catch
        {
          case e : InterruptedException => println ("Interrupted while handling input events " + e )
  //          LOGGER.log( Level.INFO, "Interrupted while handling input events", e );
          case e : InvocationTargetException => println ("Problem when handling input events " + e )
  //        LOGGER.log( Level.WARNING, "Problem when handling input events", e );
        }
    }
  */


  /**
   *        Used to update the projection that should be applied to pen input
   *        (mapping from the screen space to the canvas space)
   */
  def onProjectionChanged(xOffs: Float,
                         yOffs: Float,
                         xScale: Float,
                         yScale: Float) {
    /*
        projectionLock.synchronized{
    */
    this.xOffs = xOffs;
    this.yOffs = yOffs;
    this.xScale = xScale;
    this.yScale = yScale;
    /*
        }
    */
  }

  private def setCoordinate(dataSample: DataSample, value: Float, xAxis: Boolean) = {
    /*
          projectionLock.synchronized{
    */
    if (xAxis) {

      dataSample.setProperty(PropertyRegister.PATH_X, value * xScale + xOffs)
    }
    else {
      dataSample.setProperty(PropertyRegister.PATH_Y, value * yScale + yOffs)
    }

    /*
    }*/
  }


  def handleMouseEvent(e: MouseEvent) {

    def updateButton(dataSample: DataSample, propertyId: Int, button: Int, downMask: Int) {

      def buttonValue(downMask: Int): Float = if ((e.getModifiersEx() & downMask) == downMask) 1f else 0f

      if (e.getButton() == button) dataSample.setProperty(propertyId, buttonValue(downMask))
    }

    val dataSample = createDataSample()

    updateButton(dataSample, PropertyRegister.LEFT_BUTTON, MouseEvent.BUTTON1, InputEvent.BUTTON1_DOWN_MASK)
    updateButton(dataSample, PropertyRegister.RIGHT_BUTTON, MouseEvent.BUTTON2, InputEvent.BUTTON2_DOWN_MASK)
    updateButton(dataSample, PropertyRegister.CENTER_BUTTON, MouseEvent.BUTTON3, InputEvent.BUTTON3_DOWN_MASK)

    setCoordinate(dataSample, e.getX.toFloat, true)
    setCoordinate(dataSample, e.getY.toFloat, false)

    queueSample(dataSample)
  }


  def mouseReleased(e: MouseEvent): Unit = {
    handleMouseEvent(e)
  }


  def mousePressed(e: MouseEvent): Unit = {
    handleMouseEvent(e)
  }


  def mouseDragged(e: MouseEvent): Unit = {
    handleMouseEvent(e)
  }


  def mouseClicked(e: MouseEvent): Unit = {} // Not used
  def mouseEntered(e: MouseEvent): Unit = {} // Not used
  def mouseExited(e: MouseEvent): Unit = {} // Not used
  def mouseMoved(e: MouseEvent): Unit = {} // Not used

  def penButtonEvent(event: PButtonEvent): Unit = {
    try {
      val dataSample = createDataSample()

      val value: Float = if (event.button.value.booleanValue) 1f else 0f

      event.button.getType() match {
        case PButton.Type.LEFT => dataSample.setProperty(PropertyRegister.LEFT_BUTTON, value);
        case PButton.Type.RIGHT => dataSample.setProperty(PropertyRegister.RIGHT_BUTTON, value);
        case PButton.Type.CENTER => dataSample.setProperty(PropertyRegister.CENTER_BUTTON, value);
      }

      queueSample(dataSample);
    }
    catch {
      // Catch any exceptions here, before they go into the jpen library, as it doesn't print the original exception.
      case e: Throwable => e.printStackTrace()
    }
  }


  def penLevelEvent(event: PLevelEvent): Unit = {


    try {
      val dataSample = createDataSample()

      event.levels.foreach((level: PLevel) =>
              {
                val value: Float = level.value.floatValue;
                level.getType() match
                {
                  case PLevel.Type.X => setCoordinate(dataSample, value, true)
                  case PLevel.Type.Y => setCoordinate(dataSample, value, false)
                  case PLevel.Type.PRESSURE => dataSample.setProperty(PropertyRegister.PRESSURE, value);
                  case PLevel.Type.TILT_X => dataSample.setProperty(PropertyRegister.TILT_X, value);
                  case PLevel.Type.TILT_Y => dataSample.setProperty(PropertyRegister.TILT_Y, value);
                }
              }
        )

      queueSample(dataSample);
    }
    catch {
      // Catch any exceptions here, before they go into the jpen library, as it doesn't print the original exception.
      case e: Throwable => e.printStackTrace()
    }
  }

  def penScrollEvent(p1: PScrollEvent): Unit = {
    // Not used currently

  }

  def penTock(p1: Long): Unit = {
    // TODO: If this goes towards zero, it means our listeners are taking up too much time.
    // Reduce amount of messages?
  }

  def penKindEvent(p1: PKindEvent): Unit = {
    // Not used currently

  }

  private def queueSample(sample: DataSample) {

    try {
      sampleListener(sample)
    }
    catch {
      // Catch any exceptions here, before they go into the jpen library, as it doesn't print the original exception.
      case e: Throwable => e.printStackTrace()
    }

    /*
        try
        {
            myQueuedInput.put( sample );
        }
        catch
        {
          case e : InterruptedException =>  // Interrupted, just return.  The sample is lost, but it doesn't matter that much.
        }
    */
  }

  private def createDataSample(): DataSample = {

    val dataSample = new DataSample()

    // Maximal accuracy! :)
    dataSample.setProperty(PropertyRegister.TIME, System.nanoTime / 1000000000.0f)

    dataSample
  }


}
