package org.flowpaint


import _root_.org.flowpaint.brush._
import _root_.org.flowpaint.filters._

import _root_.org.flowpaint.ink._

import _root_.org.flowpaint.gradient.{MultiGradient, Gradient, TwoColorGradient, GradientPoint}



import _root_.org.flowpaint.property.{BrushSliderEditor, GradientSliderEditor}
import _root_.org.flowpaint.util.{DataSample, PropertyRegister}

/**
 * Specifies all the default brushes.
 *
 * @author Hans Haggstrom
 */
// TODO: Move brush descriptions to files..
object DefaultBrushFactory {

  def createDefaultBrushes() : (List[BrushSet], Brush) = {

    var brushSets :List[BrushSet] = Nil
    var currentBrush :Brush = null


    def sampleFromColor(r: Float, g: Float, b: Float, a: Float): DataSample = {

      val data = new DataSample()
      data.setProperty(PropertyRegister.RED, r)
      data.setProperty(PropertyRegister.GREEN, g)
      data.setProperty(PropertyRegister.BLUE, b)
      data.setProperty(PropertyRegister.ALPHA, a)
      data
    }
    val twoColorGradient = new TwoColorGradient(sampleFromColor(0, 0, 0.5f, 1), sampleFromColor(0, 0.5f, 1, 0.1f))

    def makeGradientPoint(position: Float, r: Float, g: Float, b: Float, a: Float): GradientPoint = {

      GradientPoint(position, sampleFromColor(r, g, b, a))
    }

    def createSmoothGradient(f: Float, v: Float): Gradient = {

      new MultiGradient(
        makeGradientPoint(0.0f, v, v, v, 0),
        makeGradientPoint(0.1f, v, v, v, 0.1f * f),
        makeGradientPoint(0.2f, v, v, v, 0.3f * f),
        makeGradientPoint(0.3f, v, v, v, 0.4f * f),
        makeGradientPoint(0.4f, v, v, v, 0.5f * f),
        makeGradientPoint(0.6f, v, v, v, 0.5f * f),
        makeGradientPoint(0.7f, v, v, v, 0.4f * f),
        makeGradientPoint(0.8f, v, v, v, 0.3f * f),
        makeGradientPoint(0.9f, v, v, v, 0.1f * f),
        makeGradientPoint(1.0f, v, v, v, 0))
    }

    def makeColoredGradient(r: Float, g: Float, b: Float, a: Float): Gradient = {

      new MultiGradient(
        makeGradientPoint(0.0f, r * 0.9f, g * 0.8f, b * 0.3f, a * 0.0f),
        makeGradientPoint(0.1f, r * 0.95f, g * 0.9f, b * 0.6f, a * 0.3f),
        makeGradientPoint(0.3f, r * 1.0f, g * 1.0f, b * 0.7f, a * 0.85f),
        makeGradientPoint(0.5f, r * 1.0f, g * 1.0f, b * 0.8f, a * 1f),
        makeGradientPoint(0.7f, r * 1.0f, g * 1.0f, b * 0.7f, a * 0.85f),
        makeGradientPoint(0.9f, r * 0.95f, g * 0.9f, b * 0.6f, a * 0.3f),
        makeGradientPoint(1.0f, r * 0.9f, g * 0.8f, b * 0.3f, a * 0.0f))
    }


    val sepiaPenGradient = makeColoredGradient(0.4f, 0.25f, 0.1f, 0.45f)
    val maroonPenGradient = makeColoredGradient(0.6f, 0.1f, 0.2f, 0.45f)
    val ocraPenGradient = makeColoredGradient(0.9f, 0.7f, 0.2f, 0.45f)
    val sapGreenPenGradient = makeColoredGradient(0.3f, 0.6f, 0.1f, 0.45f)
    val purplePenGradient = makeColoredGradient(0.2f, 0.1f, 0.6f, 0.45f)
    val lampBlackPenGradient = makeColoredGradient(0.03f, 0.08f, 0.15f, 0.45f)

    val grey = 0.35f
    val blackGradient = new MultiGradient(
      makeGradientPoint(0.0f, grey, grey, grey, 0.0f),
      makeGradientPoint(0.5f, grey, grey, grey, 0.6f),
      makeGradientPoint(1.0f, grey, grey, grey, 0.0f))

    val inkGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.15f, 0, 0.2f, 0.5f),
      makeGradientPoint(0.1f, 0, 0, 0.13f, 0.8f),
      makeGradientPoint(0.5f, 0, 0, 0, 0.9f),
      makeGradientPoint(0.9f, 0, 0, 0.13f, 0.8f),
      makeGradientPoint(1.0f, 0.1f, 0, 0.25f, 0.5f))

    val a = 0.8f
    val b = 0.6f
    val c = 0.3f
    val marbleGradient = new MultiGradient(
      makeGradientPoint(0.0f, c, c, c, 0.5f),
      makeGradientPoint(0.2f, a, a, a, 0.3f),
      makeGradientPoint(0.4f, c, c, c, 0.5f),
      makeGradientPoint(0.6f, c, c, c, 0.5f),
      makeGradientPoint(0.8f, b, b, b, 0.3f),
      makeGradientPoint(1.0f, c, c, c, 0.5f))

    val sprayGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1,1,1, 0f),
      makeGradientPoint(0.33f, 1,1,1, 0f),
      makeGradientPoint(0.4f, 1,1,1, 0.5f),
      makeGradientPoint(0.45f, 1,1,1, 0f),
      makeGradientPoint(0.9f,1,1,1, 0f),
      makeGradientPoint(0.95f, 1,1,1, 0.7f),
      makeGradientPoint(1.0f, 1,1,1, 0f))


    val dotGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0,0,0, 0f),
      makeGradientPoint(0.72f,0,0,0, 0f),
      makeGradientPoint(0.83f,0,0,0, 0.1f),
      makeGradientPoint(0.9f, 1,1,1, 0.7f),
      makeGradientPoint(1.0f, 1,1,1, 1f))


    val sunflowerGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1.0f, 1.0f, 0.2f, 1),
      makeGradientPoint(0.5f, 1.0f, 0.8f, 0.1f, 1),
      makeGradientPoint(0.9f, 0.9f, 0.5f, 0.0f, 1),
      makeGradientPoint(1.0f, 0.7f, 0.3f, 0.0f, 1)
      )

    val woodGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.4f, 0.25f, 0.15f, 1),
      makeGradientPoint(0.3f, 0.5f, 0.3f, 0.2f, 1),
      makeGradientPoint(0.31f, 0.7f, 0.6f, 0.25f, 1),
      makeGradientPoint(0.5f, 0.6f, 0.3f, 0.2f, 1),
      makeGradientPoint(0.53f, 0.75f, 0.6f, 0.3f, 1),
      makeGradientPoint(0.8f, 0.5f, 0.35f, 0.25f, 1),
      makeGradientPoint(0.82f, 0.7f, 0.6f, 0.25f, 1),
      makeGradientPoint(1.0f, 0.5f, 0.3f, 0.2f, 1)
      )

    val fireGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1.0f, 0.7f, 0.1f, 1),
      makeGradientPoint(0.2f, 1.0f, 0.4f, 0.0f, 1),
      makeGradientPoint(0.5f, 0.8f, 0.2f, 0.0f, 1f),
      makeGradientPoint(0.6f, 0.6f, 0.1f, 0.0f, 0.8f),
      makeGradientPoint(0.7f, 0.4f, 0f, 0.0f, 0f),
      makeGradientPoint(1.0f, 0, 0, 0, 0))

    val skyCloudGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0f, 0.15f, 0.65f, 0),
      makeGradientPoint(0.4f, 0f, 0.15f, 0.65f, 0),
      makeGradientPoint(0.60f, 0f, 0.15f, 0.65f, 0.1f),
      makeGradientPoint(0.63f, 0.3f, 0.5f, 0.9f, 0.9f),
      makeGradientPoint(0.68f, 1f, 1f, 1.0f, 1),
      makeGradientPoint(1.0f, 0.6f, 0.6f, 0.8f, 1)
      )

    val alienGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.1f, 0.4f, 0.2f, 1),
      makeGradientPoint(0.1f, 0.3f, 0.5f, 0.1f, 1f),
      makeGradientPoint(0.2f, 0.0f, 0.3f, 0.22f, 1f),
      makeGradientPoint(0.8f, 0.2f, 0.6f, 0.15f, 1f),
      makeGradientPoint(0.85f, 0.05f, 0.15f, 0.15f, 1f),
      makeGradientPoint(1.0f, 0.0f, 0.1f, 0.15f, 1f)
      )

    val solidBlackGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0, 0, 0, 0),
      makeGradientPoint(0.5f, 0, 0, 0, 1),
      makeGradientPoint(1.0f, 0, 0, 0, 0)
      )

    val whiteGradient = new MultiGradient(
      makeGradientPoint(0.0f, 1, 1, 1, 0f),
      makeGradientPoint(0.2f, 1, 1, 1, 0.2f),
      makeGradientPoint(0.4f, 1, 1, 1, 1f),
      makeGradientPoint(0.6f, 1, 1, 1, 1f),
      makeGradientPoint(0.8f, 1, 1, 1, 0.2f),
      makeGradientPoint(1.0f, 1, 1, 1, 0f)
      )

    val outlineGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0, 0, 0, 0f),
      makeGradientPoint(0.1f, 0, 0, 0, 1f),
      makeGradientPoint(0.15f, 0.8f, 0.8f, 0.8f, 1f),
      makeGradientPoint(0.2f, 1, 1, 1, 1f),
      makeGradientPoint(0.8f, 1, 1, 1, 1f),
      makeGradientPoint(0.85f, 0.8f, 0.8f, 0.8f, 1f),
      makeGradientPoint(0.9f, 0, 0, 0, 1f),
      makeGradientPoint(1.0f, 0, 0, 0, 0f)
      )

    val whiteBlackGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0, 0, 0, 1f),
      makeGradientPoint(1.0f, 1, 1, 1, 1f)
      )

    val charcoalGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0.4f, 0.4f, 0.4f, 0f),
      makeGradientPoint(0.4f, 0.3f, 0.3f, 0.3f, 0f),
      makeGradientPoint(0.6f, 0.1f, 0.1f, 0.1f, 0.8f),
      makeGradientPoint(1.0f, 0,0,0, 1f)
      )


    val rakeGradient = new MultiGradient(
      makeGradientPoint(0.0f, 0, 0, 0, 0),
      makeGradientPoint(0.08f, 0, 0, 0, 0),
      makeGradientPoint(0.1f, 0, 0, 0, 1),
      makeGradientPoint(0.12f, 0, 0, 0, 0),
      makeGradientPoint(0.28f, 0, 0, 0, 0),
      makeGradientPoint(0.3f, 0, 0, 0, 1),
      makeGradientPoint(0.32f, 0, 0, 0, 0),
      makeGradientPoint(0.48f, 0, 0, 0, 0),
      makeGradientPoint(0.5f, 0, 0, 0, 1),
      makeGradientPoint(0.52f, 0, 0, 0, 0),
      makeGradientPoint(0.68f, 0, 0, 0, 0),
      makeGradientPoint(0.7f, 0, 0, 0, 1),
      makeGradientPoint(0.72f, 0, 0, 0, 0),
      makeGradientPoint(0.88f, 0, 0, 0, 0),
      makeGradientPoint(0.9f, 0, 0, 0, 1),
      makeGradientPoint(0.92f, 0, 0, 0, 0),
      makeGradientPoint(1.0f, 0, 0, 0, 0)
      )

    def addFixedSizeBrush(set : FixedBrushSet, name: String, ink: Ink, radius: Float, tilt: Float): Brush = {
      val brush = new Brush(name, List(ink),
        List(
          new ZeroLengthSegmentFilter(),
          new DistanceCalculatorFilter(),
          new StrokeAngleTilter(tilt),
          new RadiusFromPressureFilter(radius, 0),
          new StrokeEdgeCalculatorFilter() ),
        Nil)

      brush.addEditor(new GradientSliderEditor("Transparency", "alpha", 1, 0, brush.getPixelProcessors()))

      brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
      brush.settings.setFloatProperty(PropertyRegister.RADIUS, radius)
      brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

      set.addBrush(brush)
      brush
    }

    def addBrush(set : FixedBrushSet, name: String, ink: Ink, radius: Float, tilt: Float, pressureEffectOnRadius: Float, includeHSLAdjust : Boolean): Brush = {
      val brush = new Brush(name, List(ink, new AdjustHSLInk() ),
        List(
          new ZeroLengthSegmentFilter(),
          new DistanceCalculatorFilter(),
          new StrokeAngleTilter(tilt),
          new RadiusFromPressureFilter(radius, pressureEffectOnRadius),
          new StrokeEdgeCalculatorFilter() ),
        Nil)

      brush.addEditor(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))
      brush.addEditor(new GradientSliderEditor("Transparency", "alpha", 1, 0, brush.getPixelProcessors()))

      if (includeHSLAdjust) {
        brush.addEditor(new BrushSliderEditor("Hue Change", "hueDelta", -0.5f, 0.5f ))
        brush.addEditor(new BrushSliderEditor("Saturation Change", "saturationDelta", -1, 1))
        brush.addEditor(new BrushSliderEditor("Lightness Change", "lightnessDelta", -1, 1))
      }

      brush.settings.setFloatProperty(PropertyRegister.HUE_DELTA, 0)
      brush.settings.setFloatProperty(PropertyRegister.SATURATION_DELTA, 0)
      brush.settings.setFloatProperty(PropertyRegister.LIGHTNESS_DELTA, 0)

      brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
      brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

      set.addBrush(brush)
      brush
    }

    def addColorBrush(set : FixedBrushSet, name: String, ink: Ink, hue:Float, saturation:Float, lightness:Float,
                     radius: Float, tilt: Float, pressureEffectOnRadius: Float): Brush = {
      val brush = new Brush(name, List(new GradientInk(whiteGradient, 0.5f), ink),
        List(
          new ZeroLengthSegmentFilter(),
          new DistanceCalculatorFilter(),
          new StrokeAngleTilter(tilt),
          new RadiusFromPressureFilter(radius, pressureEffectOnRadius),
          new StrokeEdgeCalculatorFilter() ),
        Nil)

      val transparencyInks = List(new ColorInk(), new AlphaTransparencyBackgroundInk())
      val hueInk = List(new ConstantInk(new DataSample(("alpha", 1f), ("saturation", 1f), ("lightness", 0.5f))), new ColorInk())
      val satInk = List(new ConstantInk(new DataSample(("alpha", 1f))), new ColorInk())
      val liInk = List(new ConstantInk(new DataSample(("alpha", 1f))), new ColorInk())

      brush.addEditor(new GradientSliderEditor("Hue", "hue", 0, 1, hueInk))
      brush.addEditor(new GradientSliderEditor("Saturation", "saturation", 1, 0, satInk))
      brush.addEditor(new GradientSliderEditor("Lightness", "lightness", 1, 0, liInk))
      brush.addEditor(new GradientSliderEditor("Transparency", "alpha", 1, 0, transparencyInks))
      brush.addEditor(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))

      brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
      brush.settings.setFloatProperty(PropertyRegister.HUE, hue / 360f)
      brush.settings.setFloatProperty(PropertyRegister.SATURATION, saturation)
      brush.settings.setFloatProperty(PropertyRegister.LIGHTNESS, lightness)
      brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

      set.addBrush(brush)
      brush
    }

    def addBrush2(set : FixedBrushSet, name: String, inks: List[Ink], hue:Float, saturation:Float, lightness:Float,
                     radius: Float, tilt: Float, pressureEffectOnRadius: Float): Brush = {
      val brush = new Brush(name, inks,
        List(
          new ZeroLengthSegmentFilter(),
          new DistanceCalculatorFilter(),
          new StrokeAngleTilter(tilt),
          new RadiusFromPressureFilter(radius, pressureEffectOnRadius),
          new StrokeEdgeCalculatorFilter() ),
        Nil)

      val transparencyInks = List(new ColorInk(), new AlphaTransparencyBackgroundInk())
      val hueInk = List(new ConstantInk(new DataSample(("alpha", 1f), ("saturation", 1f), ("lightness", 0.5f))), new ColorInk())
      val satInk = List(new ConstantInk(new DataSample(("alpha", 1f))), new ColorInk())
      val liInk = List(new ConstantInk(new DataSample(("alpha", 1f))), new ColorInk())

      brush.addEditor(new GradientSliderEditor("Hue", "hue", 0, 1, hueInk))
      brush.addEditor(new GradientSliderEditor("Saturation", "saturation", 1, 0, satInk))
      brush.addEditor(new GradientSliderEditor("Lightness", "lightness", 1, 0, liInk))
      brush.addEditor(new GradientSliderEditor("Transparency", "alpha", 1, 0, transparencyInks))
      brush.addEditor(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))

      brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
      brush.settings.setFloatProperty(PropertyRegister.HUE, hue / 360f)
      brush.settings.setFloatProperty(PropertyRegister.SATURATION, saturation)
      brush.settings.setFloatProperty(PropertyRegister.LIGHTNESS, lightness)
      brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

      set.addBrush(brush)
      brush
    }

    def addBrushSet( name : String ) : FixedBrushSet = {
      val set = new FixedBrushSet( name )
      brushSets = brushSets ::: List (set)
      set
    }

    val sketching = addBrushSet( "Sketching" )
    currentBrush = addBrush(sketching,"Pencil", new GradientInk(blackGradient, 1), 2.5f, 0, 0.35f, false)
    addFixedSizeBrush(sketching,"Pen", new GradientInk(solidBlackGradient, 0), 1, 0)
    addBrush(sketching,"Ink, thin", new GradientInk(inkGradient, 0), 2.7f, 0.8f, 1, false)
    addBrush(sketching,"Ink, thick", new GradientInk(inkGradient, 0), 8, 0.8f, 1, false)
    addBrush(sketching,"Crayon", new NoiseInk(charcoalGradient , (0.3f, 3f), 0.8f, "distance",3), 20, 0f, 1, true)
    addBrush(sketching,"Rake", new GradientInk(rakeGradient, 1f), 17, 0f, 0f, true)
    addBrush(sketching,"Shade, gray", new GradientInk(createSmoothGradient(0.24f, 0.3f), 1f), 23, 0, 0.5f, false)
    addBrush(sketching,"Shade, black", new GradientInk(createSmoothGradient(0.25f, 0), 1f), 70, 0, 0.5f, false)
    addBrush(sketching,"Shade, white", new GradientInk(createSmoothGradient(0.8f, 1f), 1f), 30, 0, 0.5f, false)
    addBrush(sketching,"White", new GradientInk(whiteGradient, 0), 45, 0, 1f, false)
    addBrush2(sketching, "Spray", List(new NoiseInk(sprayGradient , (10f, 10f), 1f, "distance",3),new AlphaFromPressureInk(1), new ColorInk()),0,0.5f,0, 26, 0, 0.5f)
    addBrush2(sketching, "Dots", List(new NoiseInk(dotGradient, (0.05f, 3.7f), 1f, "distance",3),new AlphaFromPressureInk(1), new ColorInk()),0,0,0.3f, 55, 0, 0f)

    val painting = addBrushSet( "Painting" )
    addColorBrush(painting, "Brown", new ColorInk(), 20f, 0.5f, 0.35f, 20, 0, 1f)
    addColorBrush(painting, "Red", new ColorInk(), 0f, 1f, 0.38f, 20, 0, 1f)
    addColorBrush(painting, "Yellow", new ColorInk(), 45f, 0.80f, 0.45f, 20, 0, 1f)
    addColorBrush(painting, "Green", new ColorInk(), 100f, 0.7f, 0.46f, 20, 0, 1f)
    addColorBrush(painting, "Blue", new ColorInk(), 215f, 1f, 0.35f, 20, 0, 1f)
    addColorBrush(painting, "Grey", new ColorInk(), 240f, 0.05f, 0.36f, 20, 0, 1f)

    val noiseBrushes = addBrushSet( "Effects" )
    addBrush(noiseBrushes, "Wood", new NoiseInk(woodGradient, (15f, 2.1f), 0.2f,"time",1), 30, 0, 0.9f, true)
    addBrush(noiseBrushes, "Fire", new NoiseInk(fireGradient, (30f, 1f), 0.35f,"time",2), 18, 0, 1f, true)
    addBrush(noiseBrushes, "Sunflower", new NoiseInk(sunflowerGradient, (15f, 2f), 0.2f), 20, 0.2f, 1, true)
    addBrush(noiseBrushes, "Grass", new NoiseInk(alienGradient, (3f, 0.5f), 0.4f,"time",2), 10, 0.3f, 1f, true)
    addBrush(noiseBrushes, "Water", new NoiseInk(twoColorGradient, (14f, 1.4f), 1f), 40, 0, 1, true)
    addBrush(noiseBrushes, "Stone", new NoiseInk(marbleGradient, (0.055f, 1.7f), 0.3f,"distance",4), 40, 0, 0.7f, true)

    addBrush(noiseBrushes, "Brown line", new GradientTestInk(0f, 1f), 10, 0, 1, true)
//    addBrush(noiseBrushes, "Purple brush", new GradientTestInk(0.5f, 1f), 30, 0, 1)
    addBrush(noiseBrushes, "Purple delight", new GradientTestInk(1f, 1f), 80, 0, 1, true)
    addBrush2(noiseBrushes, "Pop", List(new GradientInk(outlineGradient, 0), new ColorInk()), 50, 1,0.5f,   20, 0, 1)
    addBrush(noiseBrushes, "Silver brush", new NoiseInk(skyCloudGradient, (0.001f, 0.6f), 0.5f,"distance",1), 30, 0, 1f, true)
    addBrush(noiseBrushes, "Silver rain", new NoiseInk(skyCloudGradient, (0.04f, 1.5f), 0.8f,"distance",2), 50, 0, 0.5f, true)
    addBrush(noiseBrushes, "Perlin Turbulence", new NoiseInk(whiteBlackGradient, (0.05f, 1.5f), 0.5f, "distance",4), 40, 0, 1f, true)

/*
    // DEBUG
    val debugBrushes = addBrushSet( "Debuging Brushes" )
    addBrush(debugBrushes, "Dual Debug brush", new DebugInk(1,1), 80, 0, 1)
    addBrush(debugBrushes, "Along Debug brush", new DebugInk(1, 0), 80, 0, 1)
    addBrush(debugBrushes, "Across Debug brush", new DebugInk(0, 1), 80, 0, 1)
    addBrush(debugBrushes, "Solid Debug brush", new DebugInk(0, 0), 40, 0, 1)
*/


    return (brushSets, currentBrush )
  }


}