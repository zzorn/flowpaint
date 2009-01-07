package org.flowpaint.util

import scala.util.Random

/**
 *   Perlin noise function.
 *
 * @author Hans Haggstrom
 */
object PerlinNoise {



  private val random = new Random(345873214)

  // p is a permutation of all integers from 0 to 511, arranged in some random sequence
  private val p: Array[Int] = createRandomPermutationArray(256, 1337, true)

  private val BM = 0xff
  private val B = 0x1000
  private val N = 0x1000
  private var p1: Array[Int] = null
  private var g1: Array[Float] = null
  private var g2: Array[Vec2] = null

  initPerlin()



  /**
   *  1D noise generated using the original Perlin noise algorithm.
   */
  def noise1(x: Float): Float =
    {

      val t = x + N
      val bx0 = (t.toInt) & BM
      val bx1 = (bx0 + 1) & BM
      val rx0 = t - t.toInt
      val rx1 = rx0 - 1

      val sx = sCurve(rx0);

      val u = rx0 * g1(p1(bx0));
      val v = rx1 * g1(p1(bx1));

      return interpolate(sx, u, v);
    }


  /**
   *  2D noise generated with improved Perlin noise algorithm.
   */
  def noise2(x : Float, y : Float) : Float = {
    val tx = x + N
    val txInt = tx.toInt
    val bx0 = txInt & BM
    val bx1 = (bx0 + 1) & BM
    val rx0 = tx - txInt
    val rx1 = rx0 - 1

    val ty = y + N
    val tyInt = ty.toInt
    val by0 = tyInt & BM
    val by1 = (by0 + 1) & BM
    val ry0 = ty - tyInt
    val ry1 = ry0 - 1

    val i = p1(bx0)
    val j = p1(bx1)

    val b00 = p1(i + by0)
    val b10 = p1(j + by0)
    val b01 = p1(i + by1)
    val b11 = p1(j + by1)

    val sx = fade(rx0)
    val sy = fade(ry0)

    var u1 = g2(b00).dot( rx0, ry0 )
    var v1 = g2(b10).dot( rx1, ry0 )
    val a = interpolate(sx, u1, v1)

    var u2 = g2(b01).dot( rx0, ry1 )
    var v2 = g2(b11).dot( rx1, ry1 )
    val b = interpolate(sx, u2, v2)

    return interpolate(sy, a, b)
  }



  /**
   *   Three dimensional Perlin noise.
   *   Based on Ken Perlins reference implementation of improved noise ( http://mrl.nyu.edu/~perlin/noise/ ).
   *
   * @param x coordinate to calculate the noise value at
   * @param y coordinate to calculate the noise value at
   * @param z coordinate to calculate the noise value at
   *
   * @return the noise value at the specified point, a value between -1 and 1
   */
  def noise3(x: Float, y: Float, z: Float): Float = {

    def grad(hash: Int, x: Float, y: Float, z: Float): Float = {
      // Convert low 4 bits of hash code into 12 gradient directions
      val lowBits = hash & 15;
      val u = if (lowBits < 8) x else y
      val v = if (lowBits < 4) y else if (lowBits == 12 || lowBits == 14) x else z
      return (if ((lowBits & 1) == 0) u else -u) +
              (if ((lowBits & 2) == 0) v else -v)
    }



    // Calculate integer values for coordinates
    // OPTIMIZE: Math.floor seems to be somewhat slow?  Is there any faster way to get the same result?
    val roundedX = Math.floor(x).toInt
    val roundedY = Math.floor(y).toInt
    val roundedZ = Math.floor(z).toInt

    // Find unit cube that contains point
    val X: Int = roundedX & 255
    val Y: Int = roundedY & 255
    val Z: Int = roundedZ & 255

    // Find relative x,y,z of point in cube
    var x_ = x - roundedX
    var y_ = y - roundedY
    var z_ = z - roundedZ

    // Compute fade curves for each of x,y,z
    val u = fade(x_)
    val v = fade(y_)
    val w = fade(z_)

    // Hash coordinates of the 8 cube corners,
    val A = p(X) + Y
    val AA = p(A) + Z
    val AB = p(A + 1) + Z
    val B = p(X + 1) + Y
    val BA = p(B) + Z
    val BB = p(B + 1) + Z

    // and add blended results from 8 corners of cube
    return interpolate(w,
      interpolate(v,
        interpolate(u,
          grad(p(AA), x_, y_, z_),
          grad(p(BA), x_ - 1, y_, z_)),
        interpolate(u,
          grad(p(AB), x_, y_ - 1, z_),
          grad(p(BB), x_ - 1, y_ - 1, z_))),
      interpolate(v,
        interpolate(u,
          grad(p(AA + 1), x_, y_, z_ - 1),
          grad(p(BA + 1), x_ - 1, y_, z_ - 1)),
        interpolate(u,
          grad(p(AB + 1), x_, y_ - 1, z_ - 1),
          grad(p(BB + 1), x_ - 1, y_ - 1, z_ - 1))));
  }



  /**
   *  This gives mapping from 0..1 to 0..1, moving values out towards 0 and 1 more.
   */
  private def fade(t: Float): Float = {
    // fade(t) = 6t^5 - 15t^4 + 10t^3
    t * t * t * (t * (t * 6 - 15) + 10);
  }

  private def randomVectorInCircle( random : Random ) : Vec2 = {

    // TODO: This essentially creates a unit vector in a direction that is uniformly random around the circle
    // - optimize by using sin and cos instead.  For the 3D case this algorithm is probably better though.

    // Find a vector that is within the unit circle
    var squaredLen = 2f
    var x = 0f
    var y = 0f
    while (squaredLen >= 1f) {
      // Random vector in unit square
      x = random.nextFloat * 2f - 1f
      y = random.nextFloat * 2f - 1f

      squaredLen = x * x + y * y
    }

    // TODO: Should it be possible to generate 0,0?
    // Normalize
    if (squaredLen > 0f) {
      val len = Math.sqrt( squaredLen ).toFloat
      x /= len
      y /= len
    }

    Vec2(x, y)
  }


  /**
   *  Initialise the lookup arrays
   */
  private def initPerlin() {
    p1 = new Array[Int](B + B + 2)
    g1 = new Array[Float](B + B + 2)
    g2 = new Array[Vec2](B + B + 2)

    var i = 0
    var j = 0
    var k = 0

    def randomGradientComponent : Float = (random.nextInt(B + B) - B).toFloat / B


    while (i < B) {
      p1(i) = i

      g1(i) = (random.nextInt(B + B) - B).toFloat / B
      g2(i) = randomVectorInCircle( random )

      i += 1
    }

    // Permutate
    permutateArray( p1, B - 1, random )

    // Replicate to later half
    i = 0
    while (i < B + 2)
      {
        p1(B + i) = p1(i)
        g1(B + i) = g1(i)
        g2(B + i) = g2(i)

        i += 1
      }
  }

  private def permutateArray( array : Array[Int], size : Int,  random : Random ) {
    var n = size
    while (n > 1)
      {
        val k = random.nextInt( n ) // nextInt returns a value between 0 (inclusive) and n (exclusive), so k will be in the valid range 0..arrayLength -1, even if n is not.
        n -= 1

        // Swap
        val temp = array( n )
        array( n ) = array( k )
        array( k ) = temp
      }

  }

  private def createRandomPermutationArray(size: Int, seed: Long, duplicate: Boolean): Array[Int] = {

    val length = if (duplicate) size * 2 else size

    val array = new Array[Int](length)

    // Fill with range 0..size (maybe we could use the scala Range class instead somehow?)
    var i = 0
    while (i < size) {
      array(i) = i
      i += 1
    }

    // Permutate
    val random = new Random(seed)
    permutateArray( array, array.length, random )

    // Make a copy of the array if requested (spares a modulo operation?)
    if (duplicate) {
      var i = 0
      while (i < size) {
        array(i + size) = array(i)
        i += 1
      }
    }

    array
  }


  private def interpolate(t: Float, a: Float, b: Float): Float = a + t * (b - a)

  private case class Vec2( x : Float, y : Float ) {
    def dot ( other : Vec2 )  : Float = x * other.x + y * other.y
    def dot ( otherX : Float, otherY : Float ) : Float = x * otherX + y * otherY

    def squaredLength = x * x + y * y
    def length = Math.sqrt(x * x + y * y )
  }



  private def sCurve(t: Float): Float = (t * t * (3 - 2 * t))

}