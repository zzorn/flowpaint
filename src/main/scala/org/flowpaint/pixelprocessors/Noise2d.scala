package org.flowpaint.pixelprocessors

import pixelprocessor.PixelProcessor

/**
 * 
 * 
 * @author Hans Haggstrom
 */

class Noise2d extends PixelProcessor(
    """
    private static int BM = 0xff;
    private static int B = 0x1000;
    private static int N = 0x1000;
    private static int[] p1 = null;
    private static float[] g1 = null;
    private static float[][]  g2  = null;
    private static Random random = new Random(1346789);

    static {
        initPerlin2d();
    }


    /**
     *  Initialise the lookup arrays
     */
    private static void initPerlin2d() {
      p1 = new int[B + B + 2];
      g1 = new float[B + B + 2];
      g2 = new float[B + B + 2][2];

      for (int i = 0; i < B; i++) {
        p1[i] = i;

        g1[i] = ((float)(random.nextInt(B + B) - B)) / B;

        double angle = random.nextDouble() * Math.PI * 2;
        g2[i][0] = (float)(Math.cos(angle));
        g2[i][1] = (float)(Math.sin(angle));
      }

      // Permutate
      permutateArray( p1, B - 1, random );

      // Replicate to later half
      for (int i = 0; i < B + 2; i++) {
          p1[B + i] = p1[i];
          g1[B + i] = g1[i];
          g2[B + i] = g2[i];
        }
    }

    private static void permutateArray( int[] array , int size ,  Random rand ) {
      int n = size;
      while (n > 1)
        {
          int k = rand.nextInt( n ); // nextInt returns a value between 0 (inclusive) and n (exclusive), so k will be in the valid range 0..arrayLength -1, even if n is not.
          n -= 1;

          // Swap
          int temp = array[n];
          array[n] = array[k];
          array[k] = temp;
        }

    }


    private static float fadeFloat( float t )
    {
        // fade(t) = 6t^5 - 15t^4 + 10t^3
        return t * t * t * ( t * ( t * 6f - 15f ) + 10f );
    }

    /**
     *  2D noise generated with improved Perlin noise algorithm.
     */
    private final float noise2(float x, float y) {
      float tx = x + N;
      int txInt = (int)tx;
      int bx0 = txInt & BM;
      int bx1 = (bx0 + 1) & BM;
      float rx0 = tx - txInt;
      float rx1 = rx0 - 1;

      float ty = y + N;
      int tyInt = (int)ty;
      int by0 = tyInt & BM;
      int by1 = (by0 + 1) & BM;
      float ry0 = ty - tyInt;
      float ry1 = ry0 - 1;

      int i = p1[bx0];
      int j = p1[bx1];

      int b00 = p1[i + by0];
      int b10 = p1[j + by0];
      int b01 = p1[i + by1];
      int b11 = p1[j + by1];

      float sx = fadeFloat(rx0);
      float sy = fadeFloat(ry0);

      float u1 = g2[b00][0] * rx0 + g2[b00][1] * ry0;
      float v1 = g2[b10][0] * rx1 + g2[b10][1] * ry0;
      float a = interpolate(sx, u1, v1);

      float u2 = g2[b01][0] * rx0 + g2[b01][1] * ry1;
      float v2 = g2[b11][0] * rx1 + g2[b11][1] * ry1;
      float b = interpolate(sx, u2, v2);

      return interpolate(sy, a, b);
    }


    """,

    """
    """,

    """
    """) 
