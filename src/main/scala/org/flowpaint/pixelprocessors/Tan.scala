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

class Tan extends SingleFunctionPixelProcessor( Math.tan )