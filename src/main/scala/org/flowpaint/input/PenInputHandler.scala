package org.flowpaint.input

import java.lang.reflect.InvocationTargetException
import java.util.ArrayList
import java.util.concurrent.{ArrayBlockingQueue, BlockingQueue}
import jpen._
import jpen.event.PenListener
import util.DataSample
import util.PerformanceTester.time


/**
 *  Recieves pen events, and turns them into data samples, that are sent in the swing thread to the specified sampleListener.
 *
 * @author Hans Haggstrom
 */
class PenInputHandler (sampleListener : (DataSample)=>Unit)  extends PenListener {

  private var xOffs = 0f
  private var yOffs = 0f
  private var xScale = 1f
  private var yScale = 1f

  /**Used for syncronizing on the projection variables. */
  private val projectionLock = new Object

  private val MAX_INPUT_EVENT_QUEUE = 10000
  private val MAX_SAMPLES_TO_HANDLE_AT_ONCE = 100
  private val myQueuedInput : BlockingQueue[DataSample]  = new ArrayBlockingQueue[DataSample]( MAX_INPUT_EVENT_QUEUE );

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


/*
  sampleMover.start();
*/


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



  /**
   *  Used to update the projection that should be applied to pen input
   *  (mapping from the screen space to the canvas space)
   */
  def onProjectionChanged(xOffs: Float,
                         yOffs: Float,
                         xScale: Float,
                         yScale: Float) {
    projectionLock.synchronized{
      this.xOffs = xOffs;
      this.yOffs = yOffs;
      this.xScale = xScale;
      this.yScale = yScale;
    }
  }

  def penButtonEvent(event: PButtonEvent): Unit = {

    val dataSample = createDataSample(event)

    val value: Float = if (event.button.value.booleanValue ) 1f else 0f

    event.button.getType() match {
      case PButton.Type.LEFT => dataSample.setProperty("leftButton", value);
      case PButton.Type.RIGHT => dataSample.setProperty("rightButton", value);
      case PButton.Type.CENTER => dataSample.setProperty("centerButton", value);
    }

    queueSample(dataSample);
  }


  def penLevelEvent(event: PLevelEvent): Unit = {

    val dataSample = createDataSample(event)

    def setCoordinate(name: String, value: Float, scale: Float, offset: Float) =
      projectionLock.synchronized{dataSample.setProperty(name, value * scale + offset)}

    event.levels.foreach((level: PLevel) =>
            {
              val value: Float = level.value.floatValue;
              level.getType() match
              {
                case PLevel.Type.X => setCoordinate("x", value, xScale, xOffs)
                case PLevel.Type.Y => setCoordinate("y", value, yScale, yOffs)
                case PLevel.Type.PRESSURE => dataSample.setProperty("pressure", value);
                case PLevel.Type.TILT_X => dataSample.setProperty("tiltX", value);
                case PLevel.Type.TILT_Y => dataSample.setProperty("tiltY", value);
              }
            }
      )

    queueSample(dataSample);
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

  private def queueSample( sample : DataSample) {

    sampleListener(sample)
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

  private def createDataSample(event: jpen.PenEvent): DataSample = {
    val dataSample = new DataSample()
    dataSample.setProperty("time", event.getTime().toFloat / 1000.0f)
    dataSample
  }


}
