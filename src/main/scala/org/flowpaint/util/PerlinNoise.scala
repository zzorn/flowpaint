package org.flowpaint.util

/**
 *  Perlin noise function.
 *
 * @author Hans Haggstrom
 */
object PerlinNoise {


  //======================================================================
  // Private Constants

  initPerlin1()

  // TODO: Is there some array initializer idiom in scala?
  val permutation: Array[Int] = Array(151, 160, 137, 91, 90, 15,
    131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
    240, 21, 10, 23,
    190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32,
    57, 177, 33,
    88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139,
    48, 27, 166,
    77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46,
    245, 40, 244,
    102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169,
    200, 196,
    135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226,
    250, 124, 123,
    5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17,
    182, 189, 28, 42,
    223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167,
    43, 172, 9,
    129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218,
    246, 97, 228,
    251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249,
    14, 239, 107,
    49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4,
    150, 254,
    138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61,
    156, 180)

  // p is a permutation of all integers from 0 to 511, arranged in some random sequence
  val p: Array[Int] = new Array[Int](512)

  // Initialize the p array with two copies of the permutation array.
  var j = 0
  while (j < 256) {
    p(j) = permutation(j)
    p(256 + j) = p(j)
    j += 1
  }

  private def interpolate(t: Float, a: Float, b: Float): Float = a + t * (b - a)

  /**
   *  Three dimensional Perlin noise.
   *  Based on Ken Perlins reference implementation of improved noise ( http://mrl.nyu.edu/~perlin/noise/ ).
   *
   * @param x coordinate to calculate the noise value at
   * @param y coordinate to calculate the noise value at
   * @param z coordinate to calculate the noise value at
   *
   * @return the noise value at the specified point, a value between -1 and 1
   */
  def noise3(x: Float, y: Float, z: Float): Float =
    {

      /**
       * This gives mapping from 0..1 to 0..1, moving values out towards 0 and 1 more.
       */
      def fade(t: Float): Float =
        {
          // fade(t) = 6t^5 - 15t^4 + 10t^3
          t * t * t * (t * (t * 6 - 15) + 10);
        }

      def grad(hash: Int, x: Float, y: Float, z: Float): Float =
        {
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


  private val BM = 0xff
  private val B = 0x1000
  private val N = 0x1000
  private var p1: Array[Int] = null
  private var g1: Array[Float] = null
  private var g2: Array[Array[Float]] = null
  private var g3: Array[Array[Float]] = null


  /**
   * 1-D noise generation function using the original perlin algorithm.
   */
  def noise1(x: Float): Float =
    {
      def sCurve(t: Float): Float = (t * t * (3 - 2 * t))

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

  private val random = new scala.util.Random(345873214)

  /**
   * Initialise the lookup arrays used by Perlin 1 function.
   */
  private def initPerlin1() {
    p1 = new Array[Int](B + B + 2)
    g1 = new Array[Float](B + B + 2)
    g2 = new Array[Array[Float]](B + B + 2, 2)
    g3 = new Array[Array[Float]](B + B + 2, 3)
    var i = 0
    var j = 0
    var k = 0

    while (i < B) {
      p1(i) = i

      g1(i) = (random.nextInt(B + B) - B).toFloat / B

      j = 0
      while (j < 2) {
        g2(i)(j) = (random.nextInt(B + B) - B).toFloat / B
        j += 1
      }
      normalize2(g2(i))

      j = 0
      while (j < 3) {
        g3(i)(j) = (random.nextInt(B + B) - B).toFloat / B
        j += 1
      }
      normalize3(g3(i))

      i += 1
    }


    // Permutate
    i -= 1
    while (i > 0)
      {
        k = p1(i)
        j = random.nextInt(B)

        // Swap
        p1(i) = p1(j)
        p1(j) = k
        i -= 1
      }

    i = 0
    while (i < B + 2)
      {
        p1(B + i) = p1(i)
        g1(B + i) = g1(i)

        j = 0
        while (j < 2) {
          g2(B + i)(j) = g2(i)(j)
          j += 1
        }

        j = 0
        while (j < 3) {
          g3(B + i)(j) = g3(i)(j)
          j += 1
        }
        i += 1
      }
  }

  def normalize2(v: Array[Float]) {
    val s = 1f / Math.sqrt(v(0) * v(0) + v(1) * v(1)).toFloat
    v(0) *= s
    v(1) *= s
  }

  def normalize3(v: Array[Float]) {
    val s = 1f / Math.sqrt(v(0) * v(0) + v(1) * v(1) + v(2) * v(2)).toFloat
    v(0) *= s
    v(1) *= s
    v(2) *= s
  }

/*
  public float noise2(float x, float y)
            {
              float t = x + N;
              int bx0 = ((int) t) & BM;
              int bx1 = (bx0 + 1) & BM;
              float rx0 = t - (int) t;
              float rx1 = rx0 - 1;

              t = y + N;
              int by0 = ((int) t) & BM;
              int by1 = (by0 + 1) & BM;
              float ry0 = t - (int) t;
              float ry1 = ry0 - 1;

              int i = p1(bx0)
              int j = p1(bx1)

              int b00 = p1(i + by0)
              int b10 = p1(j + by0)
              int b01 = p1(i + by1)
              int b11 = p1(j + by1)

              float sx = sCurve(rx0);
              float sy = sCurve(ry0);

              float[] q = g2[b00];
              float u = rx0 * q[ 0] +ry0 * q[ 1];
  q = g2[b10];
  float v = rx1 * q[ 0] +ry0 * q[ 1];
  float a = lerp(sx, u, v);

  q = g2[b01];
  u = rx0 * q[ 0] +ry1 * q[ 1];
  q = g2[b11];
  v = rx1 * q[ 0] +ry1 * q[ 1];
  float b = lerp(sx, u, v);

  return lerp(sy, a, b);
}
*/


}