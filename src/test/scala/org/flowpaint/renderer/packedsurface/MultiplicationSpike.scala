package org.flowpaint.renderer.packedsurface


import util.TimerUtil._

/**
 * Comparing performance of int and float multiplication.
 * Float multiplication is about 1.5 times as slow as int multiplication on my 2008 model laptop, one large screen taking about 10 ms.
 * As floats are a bit easier to work with than doing some kind of fixed point with ints, it's an acceptable cost.
 * Shorts are about half the time of ints though, making them an attractive alternative.
 * Bytes are about half the time of shorts, and roughly 1/5 - 1/6 of the time of floats
 * 
 * @author Hans Haggstrom
 */

object MultiplicationSpike {
  def makeFloatLayer( w : Int, h : Int ) : Array[Float] = {
      val layer = new Array[Float](w * h)
      (0 until layer.length) foreach { layer(_) = Math.random.toFloat }
      layer
  }

  def makeIntLayer( w : Int, h : Int ) : Array[Int] = {
      val layer = new Array[Int](w * h)
      (0 until layer.length) foreach { layer(_) = (Math.random * Math.MAX_INT).toInt }
      layer
  }

  def makeShortLayer( w : Int, h : Int) : Array[Short] = {
      val layer = new Array[Short](w * h)
      (0 until layer.length) foreach { layer(_) = (Math.random * Math.MAX_SHORT).toShort }
      layer
  }

  def makeByteLayer( w : Int, h : Int) : Array[Byte] = {
      val layer = new Array[Byte](w * h)
      (0 until layer.length) foreach { layer(_) = (Math.random * Math.MAX_BYTE).toByte}
      layer
  }

  def main(args: Array[String]) {
    val w = 1600
    val h = 1200
    var floatLayer1 = makeFloatLayer(w, h)
    var intLayer1 = makeIntLayer(w, h)
    var floatLayer2 = makeFloatLayer(w, h)
    var intLayer2 = makeIntLayer(w, h)

    val repeats = 100

    timeFloatLayers( repeats, w, h )
    timeIntLayers  ( repeats, w, h )
    timeFloatLayers( repeats, w, h )
    timeIntLayers  ( repeats, w, h )
    timeFloatLayers( repeats, w, h )
    timeIntLayers  ( repeats, w, h )
    timeFloatLayers( repeats, w, h )
    timeIntLayers  ( repeats, w, h )

    timeIntLayers  ( repeats, w, h )
    timeIntLayers  ( repeats, w, h )
    timeIntLayers  ( repeats, w, h )
    timeIntLayers  ( repeats, w, h )

    timeFloatLayers( repeats, w, h )
    timeFloatLayers( repeats, w, h )
    timeFloatLayers( repeats, w, h )
    timeFloatLayers( repeats, w, h )

    timeShortLayers( repeats, w, h )
    timeShortLayers( repeats, w, h )
    timeShortLayers( repeats, w, h )
    timeShortLayers( repeats, w, h )

    timeByteLayers( repeats, w, h )
    timeByteLayers( repeats, w, h )
    timeByteLayers( repeats, w, h )
    timeByteLayers( repeats, w, h )

    print( intLayer1(w) )
    print( floatLayer1(w) )
  }

  def timeIntLayers( repeats : Int, w : Int, h : Int/*, layer1 : Array[Int], layer2 : Array[Int]*/ ) {
    var layer1 = makeIntLayer(w, h)
    var layer2 = makeIntLayer(w, h)
    val timeMs = time {
      var i = 0
      while( i < repeats ) {
        var pixel = 0
        while( pixel < w * h ) {
          layer2( pixel ) = layer2( pixel ) * layer1( pixel )
          pixel += 1
        }
        i += 1
      }
    }
    println( "Int layer of " +w+" x "+h+" took " +(timeMs / repeats.toDouble)+" ms."  )

  }

  def timeShortLayers( repeats : Int, w : Int, h : Int/*, layer1 : Array[Int], layer2 : Array[Int]*/ ) {
    var layer1 = makeShortLayer(w, h)
    var layer2 = makeShortLayer(w, h)
    val timeMs = time {
      var i = 0
      while( i < repeats ) {
        var pixel = 0
        while( pixel < w * h ) {
          layer2( pixel ) = (layer2( pixel ) * layer1( pixel )).toShort
          pixel += 1
        }
        i += 1
      }
    }
    println( "Short layer of " +w+" x "+h+" took " +(timeMs / repeats.toDouble)+" ms."  )

  }

  def timeByteLayers( repeats : Int, w : Int, h : Int/*, layer1 : Array[Int], layer2 : Array[Int]*/ ) {
    var layer1 = makeByteLayer(w, h)
    var layer2 = makeByteLayer(w, h)
    val timeMs = time {
      var i = 0
      while( i < repeats ) {
        var pixel = 0
        while( pixel < w * h ) {
          layer2( pixel ) = (layer2( pixel ) * layer1( pixel )).toByte
          pixel += 1
        }
        i += 1
      }
    }
    println( "Byte layer of " +w+" x "+h+" took " +(timeMs / repeats.toDouble)+" ms."  )

  }

  def timeFloatLayers( repeats : Int, w : Int, h : Int/*, layer1 : Array[Float], layer2 : Array[Float]*/ ) {
    var layer1 = makeFloatLayer(w, h)
    var layer2 = makeFloatLayer(w, h)
    val timeMs = time {
      var i = 0
      while( i < repeats ) {
        var pixel = 0
        while( pixel < w * h ) {
          layer2( pixel ) = layer2( pixel ) * layer1( pixel )
          pixel += 1
        }
        i += 1
      }
    }
    println( "Float layer of " +w+" x "+h+" took " +(timeMs / repeats.toDouble)+" ms."  )

  }
}

