package org.flowpaint.filters

import org.flowpaint.util.DataSample

/**
 * A StrokeFilter is a way to modify a stroke path as it is being created.
 * The filter is given one sample at a time, and returns zero or more samples.
 * The filter is also notified when the sample is the first or last on the stroke.
 *
 * @author Hans Haggstrom
 */
// TODO: Maybe some way to build on multiple strokes at once?  Or multiple sections of a stroke..
// TODO: Introduce several paths to one stroke?  Could e.g. also be used for picture tubes / spray paint?
// Would be nice to unify strokes and picture splatter anyway
// Multiple strokes would also support multi-bristle brushes (way cool)
trait StrokeFilter {

  final def filterStrokePoint( pointData : DataSample, nextFilters: List[StrokeFilter], finalListener: StrokeListener  ) {

    filterStrokePoint( pointData,
      (result : DataSample) => if (nextFilters.isEmpty) finalListener.addStrokePoint(result)
              else nextFilters.head.filterStrokePoint( result, nextFilters.tail, finalListener ) )

  }

  protected def filterStrokePoint( pointData : DataSample, resultCallback : DataSample => Unit )

}