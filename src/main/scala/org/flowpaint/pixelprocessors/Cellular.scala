package org.flowpaint.pixelprocessors
import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.PixelProcessor
import util.DataSample

/**
 * Uses a grid, where each grid cell has one random point within the cell.  The value for each x,y coordinate is the distance to the closest grid center.
 * Should be relatively fast to calculate.
 *
 * @author Hans Haggstrom
 */
// TODO: Add random seed from user too?
// TODO: Extract the concept of a point cloud with fast find closest and next closest operations, and random seeds for points?
// Would allow for different distributions, and more control over it.  Useful e.g. for terrain and texture generation.
object Cellular{

  def generateGridCellInitializer( index : Int,
                                 offsetX : String, offsetY : String,
                                 offsetXId : String, offsetYId : String  ) : String = {
    """
      random$id$.setSeed( gridIndexX$id$ """+offsetX+""" );
      random$id$.setSeed( gridIndexY$id$ """+offsetY+""" ^ random$id$.nextLong() );
      neighbourSeed$id$["""+index+"""] = random$id$.nextLong();
      neighbourX$id$["""+index+"""] = gridX"""+offsetXId+"""$id$ + gridSizeX$id$ * random$id$.nextFloat();
      neighbourY$id$["""+index+"""] = gridY"""+offsetYId+"""$id$ + gridSizeY$id$ * random$id$.nextFloat();
      dx$id$ = neighbourX$id$["""+index+"""] - x$id$;
      dy$id$ = neighbourY$id$["""+index+"""] - y$id$;
      neighbourDistanceSquared$id$["""+index+"""] = dx$id$ * dx$id$ + dy$id$ * dy$id$;

    """
  }

  def findClosestNeighbours( index : Int ) : String = {
    """
     if ( ! (closestNeighbourDistanceSquared$id$ < neighbourDistanceSquared$id$["""+index+"""] )) {
        nextNeighbourSeed$id$ = closestNeighbourSeed$id$ ;
        nextNeighbourX$id$ = closestNeighbourX$id$;
        nextNeighbourY$id$ = closestNeighbourY$id$;
        nextNeighbourDistanceSquared$id$ = closestNeighbourDistanceSquared$id$;

        closestNeighbourSeed$id$ = neighbourSeed$id$["""+index+"""];
        closestNeighbourX$id$ = neighbourX$id$["""+index+"""];
        closestNeighbourY$id$ = neighbourY$id$["""+index+"""];
        closestNeighbourDistanceSquared$id$ = neighbourDistanceSquared$id$["""+index+"""];
     }
     else if ( ! (nextNeighbourDistanceSquared$id$ < neighbourDistanceSquared$id$["""+index+"""]) ) {
        nextNeighbourSeed$id$ = neighbourSeed$id$["""+index+"""];
        nextNeighbourX$id$ = neighbourX$id$["""+index+"""];
        nextNeighbourY$id$ = neighbourY$id$["""+index+"""];
        nextNeighbourDistanceSquared$id$ = neighbourDistanceSquared$id$["""+index+"""];
     }

     
    """
  }
}

class Cellular extends PixelProcessor(
  """
    private final Random random$id$ = new Random(4357);

    private final long[] neighbourSeed$id$ = new long[9];
    private final float[] neighbourX$id$ = new float[9];
    private final float[] neighbourY$id$ = new float[9];
    private final float[] neighbourDistanceSquared$id$ = new float[9]; 

  """,
  "",
  """
    final float gridSizeX$id$  = $getScaleOffsetFloat gridSizeX, 32$;
    final float gridSizeY$id$  = $getScaleOffsetFloat gridSizeY, 32$;
    final float x$id$  = $getScaleOffsetFloat canvasX, 0f$;
    final float y$id$  = $getScaleOffsetFloat canvasY, 0f$;

    // Determine the grid cell that the current pixel is in
    final int gridIndexX$id$ = (gridSizeX$id$ == 0) ? 0 : (int)(Math.floor(x$id$ / gridSizeX$id$));
    final int gridIndexY$id$ = (gridSizeY$id$ == 0) ? 0 : (int)(Math.floor(y$id$ / gridSizeY$id$));

    final float gridXa$id$ = gridSizeX$id$ * (gridIndexX$id$ - 1);
    final float gridXb$id$ = gridSizeX$id$ * (gridIndexX$id$);
    final float gridXc$id$ = gridSizeX$id$ * (gridIndexX$id$ + 1);

    final float gridYa$id$ = gridSizeY$id$ * (gridIndexY$id$ - 1);
    final float gridYb$id$ = gridSizeY$id$ * (gridIndexY$id$);
    final float gridYc$id$ = gridSizeY$id$ * (gridIndexY$id$ + 1);

    float dx$id$ = 0f;
    float dy$id$ = 0f;

    // Calculate positions of centers in adjacent grid cells
    """ +
     Cellular.generateGridCellInitializer(0, "- 1", "- 1", "a", "a") +
     Cellular.generateGridCellInitializer(1, "   ", "- 1", "b", "a") +
     Cellular.generateGridCellInitializer(2, "+ 1", "- 1", "c", "a") +
     Cellular.generateGridCellInitializer(3, "- 1", "   ", "a", "b") +
     Cellular.generateGridCellInitializer(4, "   ", "   ", "b", "b") +
     Cellular.generateGridCellInitializer(5, "+ 1", "   ", "c", "b") +
     Cellular.generateGridCellInitializer(6, "- 1", "+ 1", "a", "c") +
     Cellular.generateGridCellInitializer(7, "   ", "+ 1", "b", "c") +
     Cellular.generateGridCellInitializer(8, "+ 1", "+ 1", "c", "c") +
    """

    // Find closest two grid points
    long closestNeighbourSeed$id$ = 0;
    float closestNeighbourX$id$ = 0f;
    float closestNeighbourY$id$ = 0f;
    float closestNeighbourDistanceSquared$id$ = Float.NaN;

    long nextNeighbourSeed$id$ = 0;
    float nextNeighbourX$id$ = 0f;
    float nextNeighbourY$id$ = 0f;
    float nextNeighbourDistanceSquared$id$ = Float.NaN;

    """ +
     Cellular.findClosestNeighbours( 0 ) +
     Cellular.findClosestNeighbours( 1 ) +
     Cellular.findClosestNeighbours( 2 ) +
     Cellular.findClosestNeighbours( 3 ) +
     Cellular.findClosestNeighbours( 4 ) +
     Cellular.findClosestNeighbours( 5 ) +
     Cellular.findClosestNeighbours( 6 ) +
     Cellular.findClosestNeighbours( 7 ) +
     Cellular.findClosestNeighbours( 8 ) +
    """

    // Calculate results
    final float closestDistance$id$ = (float) Math.sqrt( closestNeighbourDistanceSquared$id$ );
    final float nextDistance$id$    = (float) Math.sqrt( nextNeighbourDistanceSquared$id$ );
    final float totalDistance$id$ = (closestDistance$id$ + nextDistance$id$) * 0.5f;
    final float relativeDistance$id$ =  (totalDistance$id$ == 0f) ? 0f : closestDistance$id$ / totalDistance$id$;

    random$id$.setSeed( closestNeighbourSeed$id$ );
    final float closestRandom$id$ = random$id$.nextFloat();

    random$id$.setSeed( nextNeighbourSeed$id$ );
    final float nextRandom$id$ = random$id$.nextFloat();

    $setScaleOffsetFloat relativeDistance$ relativeDistance$id$;
    $setScaleOffsetFloat closestDistance$  closestDistance$id$;
    $setScaleOffsetFloat closestX$         closestNeighbourX$id$;
    $setScaleOffsetFloat closestY$         closestNeighbourY$id$;
//    $setScaleOffsetFloat closestSeed$      closestNeighbourSeed$id$;
    $setScaleOffsetFloat closestRandom$    closestRandom$id$;
    $setScaleOffsetFloat nextDistance$     nextDistance$id$;
    $setScaleOffsetFloat nextX$            nextNeighbourX$id$;
    $setScaleOffsetFloat nextY$            nextNeighbourY$id$;
//    $setScaleOffsetFloat nextSeed$         nextNeighbourSeed$id$;
    $setScaleOffsetFloat nextRandom$       nextRandom$id$;
  """) {


  def processPixel(variables: DataSample, variableNameMappings: Map[String, String], generalSettings: Data) = {}
}