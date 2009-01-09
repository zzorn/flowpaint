package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.Data
import _root_.scala.collection.Map
import pixelprocessor.{PixelProcessor, SingleFunctionPixelProcessor}
import util.DataSample
/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Signum extends  SingleFunctionPixelProcessor( Math.signum, "Math.signum" )