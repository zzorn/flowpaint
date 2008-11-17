package org.flowpaint.util

import org.junit._
import Assert._

/**
 *
 *
 * @author Hans Haggstrom
 */
@Test
class MathUtilTest {
  val EPSILON: Double = 0.0001


  @Test
  def testNormalizeAngle() {

    assertEquals(0, MathUtils.normalizeAngle(0), EPSILON)
    assertEquals(0, MathUtils.normalizeAngle(Math.Pi * 2), EPSILON)

    assertEquals(0.25, MathUtils.normalizeAngle(Math.Pi * 0.5), EPSILON)
    assertEquals(0.5, MathUtils.normalizeAngle(Math.Pi * 1), EPSILON)
    assertEquals(0.75, MathUtils.normalizeAngle(Math.Pi * 1.5), EPSILON)

    assertEquals(0, MathUtils.normalizeAngle(Math.Pi * -2), EPSILON)
    assertEquals(0.75, MathUtils.normalizeAngle(Math.Pi * -0.5), EPSILON)
    assertEquals(0.5, MathUtils.normalizeAngle(Math.Pi * -1), EPSILON)
    assertEquals(0.25, MathUtils.normalizeAngle(Math.Pi * -1.5), EPSILON)

    assertEquals(0.25, MathUtils.normalizeAngle(Math.Pi * 2.5), EPSILON)
    assertEquals(0.5, MathUtils.normalizeAngle(Math.Pi * 3), EPSILON)
    assertEquals(0.75, MathUtils.normalizeAngle(Math.Pi * 3.5), EPSILON)
    assertEquals(0, MathUtils.normalizeAngle(Math.Pi * 4), EPSILON)
  }

  @Test
  def testWrap() {
    assertEquals(0, MathUtils.wrapToZeroToOne(0), EPSILON)
    assertEquals(0.1, MathUtils.wrapToZeroToOne(0.1f), EPSILON)
    assertEquals(0.8, MathUtils.wrapToZeroToOne(0.8f), EPSILON)
    assertEquals(0.99, MathUtils.wrapToZeroToOne(0.99f), EPSILON)
    assertEquals(0, MathUtils.wrapToZeroToOne(1), EPSILON)

    assertEquals(0.1, MathUtils.wrapToZeroToOne(1.1f), EPSILON)
    assertEquals(0.9, MathUtils.wrapToZeroToOne(-0.1f), EPSILON)
    assertEquals(0.7, MathUtils.wrapToZeroToOne(-1.3f), EPSILON)
    assertEquals(0.3, MathUtils.wrapToZeroToOne(1.3f), EPSILON)
    assertEquals(0.7, MathUtils.wrapToZeroToOne(2.7f), EPSILON)
    assertEquals(0, MathUtils.wrapToZeroToOne(10000), EPSILON)
  }

  @Test
  def testWrapDistance() {

    assertEquals(0, MathUtils.wrappedDistance(0, 0), EPSILON)
    assertEquals(0, MathUtils.wrappedDistance(0.5f, 0.5f), EPSILON)
    assertEquals(0, MathUtils.wrappedDistance(1, 1), EPSILON)

    assertEquals(0.5, MathUtils.wrappedDistance(0, 0.5f), EPSILON)
    assertEquals(0.5, MathUtils.wrappedDistance(1, 0.5f), EPSILON)

    assertEquals(0, MathUtils.wrappedDistance(1, 0), EPSILON)
    assertEquals(0, MathUtils.wrappedDistance(0, 1), EPSILON)

    assertEquals(0.3, MathUtils.wrappedDistance(0, 0.3f), EPSILON)
    assertEquals(0.3, MathUtils.wrappedDistance(0.3f, 0.6f), EPSILON)
    assertEquals(0.3, MathUtils.wrappedDistance(0.7f, 1), EPSILON)
    assertEquals(0.3, MathUtils.wrappedDistance(0.7f, 0), EPSILON)
    assertEquals(0.3, MathUtils.wrappedDistance(0.8f, 0.1f), EPSILON)
    assertEquals(0.3, MathUtils.wrappedDistance(0.2f, 0.9f), EPSILON)
  }

  @Test
  def testWrappedInterpolate() {

    assertEquals(0, MathUtils.wrappedInterpolate(0, 0, 1), EPSILON)
    assertEquals(0, MathUtils.wrappedInterpolate(0.6f, 0, 1), EPSILON)

    assertEquals(0.2, MathUtils.wrappedInterpolate(0.5f, 0, 0.4f), EPSILON)
    assertEquals(0.8, MathUtils.wrappedInterpolate(2f, 0, 0.4f), EPSILON)
    assertEquals(0.6, MathUtils.wrappedInterpolate(4f, 0, 0.4f), EPSILON)
    assertEquals(0.8, MathUtils.wrappedInterpolate(-0.5f, 0, 0.4f), EPSILON)

    assertEquals(0.8, MathUtils.wrappedInterpolate(0f, 0.8f, 0.2f), EPSILON)
    assertEquals(0.9, MathUtils.wrappedInterpolate(0.25f, 0.8f, 0.2f), EPSILON)
    assertEquals(0.0, MathUtils.wrappedInterpolate(0.5f, 0.8f, 0.2f), EPSILON)
    assertEquals(0.1, MathUtils.wrappedInterpolate(0.75f, 0.8f, 0.2f), EPSILON)
    assertEquals(0.2, MathUtils.wrappedInterpolate(1f, 0.8f, 0.2f), EPSILON)
  }

}