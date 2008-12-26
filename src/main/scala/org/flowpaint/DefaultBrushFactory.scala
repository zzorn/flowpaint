package org.flowpaint


import _root_.org.flowpaint.brush._
import _root_.org.flowpaint.filters._

import _root_.org.flowpaint.ink._

import _root_.org.flowpaint.gradient.{MultiGradient, Gradient, GradientPoint}



import _root_.org.flowpaint.property.{BrushSliderEditor, GradientSliderEditor, Data, DataImpl}
import _root_.scala.xml.{Elem, XML, Node}
import util.{DataSample, ListenableList, ResourceLoader, PropertyRegister}

/**
 *  Specifies all the default brushes.
 *
 * @author Hans Haggstrom
 */
// TODO: Move brush descriptions to files..
object DefaultBrushFactory {
    def createDefaultBrushes(): (List[BrushSet], Brush) = {

        // Read from xml

        val defaultFlowpaintData: Elem = ResourceLoader.loadXml("default-brushes.xml", "default brushes", <flowpaint/>)

        FlowPaint.library.fromXML(defaultFlowpaintData)

        var brushSets: List[BrushSet] = List(new FixedBrushSet("Default Brushes", 32, Nil))
        var currentBrush: Brush = FlowPaint.library.getTome("Pencil", null)


        def sampleFromColor(r: Float, g: Float, b: Float, a: Float): Data = {

            val data = new DataImpl()
            data.setFloatProperty(PropertyRegister.RED, r)
            data.setFloatProperty(PropertyRegister.GREEN, g)
            data.setFloatProperty(PropertyRegister.BLUE, b)
            data.setFloatProperty(PropertyRegister.ALPHA, a)
            data
        }

        def addGradient(g : Gradient) {
          FlowPaint.library.putTome(g)
        }

        def makeGradientPoint(position: Float, r: Float, g: Float, b: Float, a: Float): GradientPoint = {

            GradientPoint(position, sampleFromColor(r, g, b, a))
        }

        val twoColorGradient = new MultiGradient("default.gradients.blues",
             makeGradientPoint(0.0f, 0, 0, 0.5f, 1),
             makeGradientPoint(0.0f, 0, 0.5f, 1, 0.1f))

         def createSmoothGradient(identifier: String, f: Float, v: Float): Gradient = {

            val g = new MultiGradient("default.gradients." + identifier,
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
           addGradient(g)
           g
        }
        val smoothGrey = createSmoothGradient("smoothGrey", 0.24f, 0.3f)
        val smoothBlack = createSmoothGradient("smoothBlack", 0.25f, 0f)
        val smoothWhite = createSmoothGradient("smoothWhite", 0.8f, 1f)

        def makeColoredGradient(identifier: String, r: Float, g: Float, b: Float, a: Float): Gradient = {

            val gr = new MultiGradient("default.gradients." + identifier,
                makeGradientPoint(0.0f, r * 0.9f, g * 0.8f, b * 0.3f, a * 0.0f),
                makeGradientPoint(0.1f, r * 0.95f, g * 0.9f, b * 0.6f, a * 0.3f),
                makeGradientPoint(0.3f, r * 1.0f, g * 1.0f, b * 0.7f, a * 0.85f),
                makeGradientPoint(0.5f, r * 1.0f, g * 1.0f, b * 0.8f, a * 1f),
                makeGradientPoint(0.7f, r * 1.0f, g * 1.0f, b * 0.7f, a * 0.85f),
                makeGradientPoint(0.9f, r * 0.95f, g * 0.9f, b * 0.6f, a * 0.3f),
                makeGradientPoint(1.0f, r * 0.9f, g * 0.8f, b * 0.3f, a * 0.0f))
           addGradient(gr)
          gr
        }


        val sepiaPenGradient = makeColoredGradient("sepia", 0.4f, 0.25f, 0.1f, 0.45f)
        val maroonPenGradient = makeColoredGradient("maroon", 0.6f, 0.1f, 0.2f, 0.45f)
        val ocraPenGradient = makeColoredGradient("ocra", 0.9f, 0.7f, 0.2f, 0.45f)
        val sapGreenPenGradient = makeColoredGradient("sapGreen", 0.3f, 0.6f, 0.1f, 0.45f)
        val purplePenGradient = makeColoredGradient("steel", 0.2f, 0.1f, 0.6f, 0.45f)
        val lampBlackPenGradient = makeColoredGradient("lampBlack", 0.03f, 0.08f, 0.15f, 0.45f)

        val grey = 0.35f
        val blackGradient = new MultiGradient("default.gradients.black",
            makeGradientPoint(0.0f, grey, grey, grey, 0.0f),
            makeGradientPoint(0.5f, grey, grey, grey, 0.6f),
            makeGradientPoint(1.0f, grey, grey, grey, 0.0f))
        addGradient(blackGradient)


        val inkGradient = new MultiGradient("default.gradients.ink",
            makeGradientPoint(0.0f, 0.15f, 0, 0.2f, 0.5f),
            makeGradientPoint(0.1f, 0, 0, 0.13f, 0.8f),
            makeGradientPoint(0.5f, 0, 0, 0, 0.9f),
            makeGradientPoint(0.9f, 0, 0, 0.13f, 0.8f),
            makeGradientPoint(1.0f, 0.1f, 0, 0.25f, 0.5f))
        addGradient(inkGradient)

        val a = 0.8f
        val b = 0.6f
        val c = 0.3f
        val marbleGradient = new MultiGradient("default.gradients.stone",
            makeGradientPoint(0.0f, c, c, c, 0.5f),
            makeGradientPoint(0.2f, a, a, a, 0.3f),
            makeGradientPoint(0.4f, c, c, c, 0.5f),
            makeGradientPoint(0.6f, c, c, c, 0.5f),
            makeGradientPoint(0.8f, b, b, b, 0.3f),
            makeGradientPoint(1.0f, c, c, c, 0.5f))
        addGradient(marbleGradient)

        val sprayGradient = new MultiGradient("default.gradients.spray",
            makeGradientPoint(0.0f, 1, 1, 1, 0f),
            makeGradientPoint(0.33f, 1, 1, 1, 0f),
            makeGradientPoint(0.4f, 1, 1, 1, 0.5f),
            makeGradientPoint(0.45f, 1, 1, 1, 0f),
            makeGradientPoint(0.9f, 1, 1, 1, 0f),
            makeGradientPoint(0.95f, 1, 1, 1, 0.7f),
            makeGradientPoint(1.0f, 1, 1, 1, 0f))
        addGradient(sprayGradient)


        val dotGradient = new MultiGradient("default.gradients.dot",
            makeGradientPoint(0.0f, 0, 0, 0, 0f),
            makeGradientPoint(0.72f, 0, 0, 0, 0f),
            makeGradientPoint(0.83f, 0, 0, 0, 0.1f),
            makeGradientPoint(0.9f, 1, 1, 1, 0.7f),
            makeGradientPoint(1.0f, 1, 1, 1, 1f))
        addGradient(dotGradient)


        val sunflowerGradient = new MultiGradient("default.gradients.sunflower",
            makeGradientPoint(0.0f, 1.0f, 1.0f, 0.2f, 1),
            makeGradientPoint(0.5f, 1.0f, 0.8f, 0.1f, 1),
            makeGradientPoint(0.9f, 0.9f, 0.5f, 0.0f, 1),
            makeGradientPoint(1.0f, 0.7f, 0.3f, 0.0f, 1)
            )
        addGradient(sunflowerGradient)
         
        val woodGradient = new MultiGradient("default.gradients.wood",
            makeGradientPoint(0.0f, 0.4f, 0.25f, 0.15f, 1),
            makeGradientPoint(0.3f, 0.5f, 0.3f, 0.2f, 1),
            makeGradientPoint(0.31f, 0.7f, 0.6f, 0.25f, 1),
            makeGradientPoint(0.5f, 0.6f, 0.3f, 0.2f, 1),
            makeGradientPoint(0.53f, 0.75f, 0.6f, 0.3f, 1),
            makeGradientPoint(0.8f, 0.5f, 0.35f, 0.25f, 1),
            makeGradientPoint(0.82f, 0.7f, 0.6f, 0.25f, 1),
            makeGradientPoint(1.0f, 0.5f, 0.3f, 0.2f, 1)
            )
        addGradient(woodGradient)

        val fireGradient = new MultiGradient("default.gradients.fire",
            makeGradientPoint(0.0f, 1.0f, 0.7f, 0.1f, 1),
            makeGradientPoint(0.2f, 1.0f, 0.4f, 0.0f, 1),
            makeGradientPoint(0.5f, 0.8f, 0.2f, 0.0f, 1f),
            makeGradientPoint(0.6f, 0.6f, 0.1f, 0.0f, 0.8f),
            makeGradientPoint(0.7f, 0.4f, 0f, 0.0f, 0f),
            makeGradientPoint(1.0f, 0, 0, 0, 0))
        addGradient(fireGradient)

        val skyCloudGradient = new MultiGradient("default.gradients.sky",
            makeGradientPoint(0.0f, 0f, 0.15f, 0.65f, 0),
            makeGradientPoint(0.4f, 0f, 0.15f, 0.65f, 0),
            makeGradientPoint(0.60f, 0f, 0.15f, 0.65f, 0.1f),
            makeGradientPoint(0.63f, 0.3f, 0.5f, 0.9f, 0.9f),
            makeGradientPoint(0.68f, 1f, 1f, 1.0f, 1),
            makeGradientPoint(1.0f, 0.6f, 0.6f, 0.8f, 1)
            )
        addGradient(skyCloudGradient)

        val alienGradient = new MultiGradient("default.gradients.leaf",
            makeGradientPoint(0.0f, 0.1f, 0.4f, 0.2f, 1),
            makeGradientPoint(0.1f, 0.3f, 0.5f, 0.1f, 1f),
            makeGradientPoint(0.2f, 0.0f, 0.3f, 0.22f, 1f),
            makeGradientPoint(0.8f, 0.2f, 0.6f, 0.15f, 1f),
            makeGradientPoint(0.85f, 0.05f, 0.15f, 0.15f, 1f),
            makeGradientPoint(1.0f, 0.0f, 0.1f, 0.15f, 1f)
            )
        addGradient(alienGradient)

        val solidBlackGradient = new MultiGradient("default.gradients.solidBlack",
            makeGradientPoint(0.0f, 0, 0, 0, 0),
            makeGradientPoint(0.5f, 0, 0, 0, 1),
            makeGradientPoint(1.0f, 0, 0, 0, 0)
            )
        addGradient(solidBlackGradient)

        val whiteGradient = new MultiGradient("default.gradients.white",
            makeGradientPoint(0.0f, 1, 1, 1, 0f),
            makeGradientPoint(0.2f, 1, 1, 1, 0.2f),
            makeGradientPoint(0.4f, 1, 1, 1, 1f),
            makeGradientPoint(0.6f, 1, 1, 1, 1f),
            makeGradientPoint(0.8f, 1, 1, 1, 0.2f),
            makeGradientPoint(1.0f, 1, 1, 1, 0f)
            )
        addGradient(whiteGradient)

        val outlineGradient = new MultiGradient("default.gradients.outline",
            makeGradientPoint(0.0f, 0, 0, 0, 0f),
            makeGradientPoint(0.1f, 0, 0, 0, 1f),
            makeGradientPoint(0.15f, 0.8f, 0.8f, 0.8f, 1f),
            makeGradientPoint(0.2f, 1, 1, 1, 1f),
            makeGradientPoint(0.8f, 1, 1, 1, 1f),
            makeGradientPoint(0.85f, 0.8f, 0.8f, 0.8f, 1f),
            makeGradientPoint(0.9f, 0, 0, 0, 1f),
            makeGradientPoint(1.0f, 0, 0, 0, 0f)
            )
        addGradient(outlineGradient)

        val whiteBlackGradient = new MultiGradient("default.gradients.whiteBlack",
            makeGradientPoint(0.0f, 0, 0, 0, 1f),
            makeGradientPoint(1.0f, 1, 1, 1, 1f)
            )
        addGradient(whiteBlackGradient)

        val charcoalGradient = new MultiGradient("default.gradients.charcoal",
            makeGradientPoint(0.0f, 0.4f, 0.4f, 0.4f, 0f),
            makeGradientPoint(0.4f, 0.3f, 0.3f, 0.3f, 0f),
            makeGradientPoint(0.6f, 0.1f, 0.1f, 0.1f, 0.8f),
            makeGradientPoint(1.0f, 0, 0, 0, 1f)
            )
        addGradient(charcoalGradient)


        val rakeGradient = new MultiGradient("default.gradients.rake",
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
        addGradient(rakeGradient)

      def makeSettings( settings : Seq[(String,Float)] ) : Data ={
          val set = new DataImpl()
          settings foreach ( (e:(String,Float)) => set.setFloatProperty( e._1, e._2 ) )
          set
      }

        def createInkMetadata[T <: Ink](classType : Class[T], settings: Data): PixelProcessorMetadata = {
            new PixelProcessorMetadata(classType, settings)
        }

        def createInkMetadata2[T <: Ink](classType : Class[T], settings: (String, Float)*): PixelProcessorMetadata = {
            new PixelProcessorMetadata(classType, makeSettings(settings))
        }

        def createFilterMetadata[T <: PathProcessor](classType : Class[T],  settings: (String, Float)*): PathProcessorMetadata= {
            new PathProcessorMetadata(classType, makeSettings(settings))
        }

        def addFixedSizeBrush(set: FixedBrushSet, name: String, ink: PixelProcessorMetadata, radius: Float, tilt: Float): Brush = {
            val brush = new Brush(name, new DataImpl(), List(ink),
                List(
                    createFilterMetadata(classOf[ZeroLengthSegmentFilter]),
                    createFilterMetadata(classOf[DistanceCalculatorFilter]),
                    createFilterMetadata(classOf[StrokeAngleTilter],("tilt", tilt)),
                    createFilterMetadata(classOf[RadiusFromPressureFilter],("maxRadius", radius), ("pressureEffect", 0f)),
                    createFilterMetadata(classOf[StrokeEdgeCalculatorFilter]) ),
                Nil)

            brush.editors.add(new GradientSliderEditor("Transparency", "alpha", 1, 0, brush.pixelProcessors))

            brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
            brush.settings.setFloatProperty(PropertyRegister.RADIUS, radius)
            brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1f)

            set.addBrush(brush)
            brush
        }

        def addBrush(set: FixedBrushSet, name: String, ink: PixelProcessorMetadata, radius: Float, tilt: Float, pressureEffectOnRadius: Float, includeHSLAdjust: Boolean, gradient:String): Brush = {
            val brush = new Brush(name, new DataImpl(), List(ink, createInkMetadata(classOf[AdjustHSLInk], new DataImpl() )),
                List(
                    createFilterMetadata(classOf[ZeroLengthSegmentFilter]),
                    createFilterMetadata(classOf[DistanceCalculatorFilter]),
                    createFilterMetadata(classOf[StrokeAngleTilter],("tilt", tilt)),
                    createFilterMetadata(classOf[RadiusFromPressureFilter],("pressureEffect", pressureEffectOnRadius)),
                    createFilterMetadata(classOf[StrokeEdgeCalculatorFilter]) ),
                Nil)

            brush.editors.add(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))
            brush.editors.add(new GradientSliderEditor("Transparency", "alpha", 1, 0, brush.pixelProcessors))

            if (includeHSLAdjust) {
                brush.editors.add(new BrushSliderEditor("Hue Change", "hueDelta", -0.5f, 0.5f))
                brush.editors.add(new BrushSliderEditor("Saturation Change", "saturationDelta", -1, 1))
                brush.editors.add(new BrushSliderEditor("Lightness Change", "lightnessDelta", -1, 1))
            }

            brush.settings.setFloatProperty(PropertyRegister.HUE_DELTA, 0)
            brush.settings.setFloatProperty(PropertyRegister.SATURATION_DELTA, 0)
            brush.settings.setFloatProperty(PropertyRegister.LIGHTNESS_DELTA, 0)

            brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
            brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

            brush.settings.setReference("gradient", "default.gradients."+gradient)

            set.addBrush(brush)
            brush
        }

        def addColorBrush(set: FixedBrushSet, name: String, ink: org.flowpaint.ink.PixelProcessorMetadata, hue: Float, saturation: Float, lightness: Float,
                         radius: Float, tilt: Float, pressureEffectOnRadius: Float): Brush = {

          val settings = new DataImpl()
          settings.setFloatProperty( "alphaPressure", 0.5f )
          settings.setStringProperty( "gradient", "default.gradients.white" )

            val brush = new Brush(name, new DataImpl(), List(createInkMetadata(classOf[GradientInk], settings), ink),
                List(
                    createFilterMetadata(classOf[ZeroLengthSegmentFilter]),
                    createFilterMetadata(classOf[DistanceCalculatorFilter]),
                    createFilterMetadata(classOf[StrokeAngleTilter],("tilt", tilt)),
                    createFilterMetadata(classOf[RadiusFromPressureFilter],("maxRadius", radius), ("pressureEffect", pressureEffectOnRadius)),
                    createFilterMetadata(classOf[StrokeEdgeCalculatorFilter]) ),
                Nil)

            val transparencyInks = new ListenableList(List(
                createInkMetadata(classOf[ColorInk],new DataImpl()),
                createInkMetadata(classOf[AlphaTransparencyBackgroundInk],new DataImpl()) ))
            val hueInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f),
                        ("saturation", 1f),
                        ("lightness", 0.5f))),
                createInkMetadata(classOf[ColorInk] ,new DataImpl()) ))
            val satInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f))),
                createInkMetadata(classOf[ColorInk],new DataImpl()) ))
            val liInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f))),
                createInkMetadata(classOf[ColorInk],new DataImpl()) ))

            brush.editors.add(new GradientSliderEditor("Hue", "hue", 0, 1, hueInk))
            brush.editors.add(new GradientSliderEditor("Saturation", "saturation", 1, 0, satInk))
            brush.editors.add(new GradientSliderEditor("Lightness", "lightness", 1, 0, liInk))
            brush.editors.add(new GradientSliderEditor("Transparency", "alpha", 1, 0, transparencyInks))
            brush.editors.add(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))

            brush.settings.setFloatProperty(PropertyRegister.MAX_RADIUS, radius)
            brush.settings.setFloatProperty(PropertyRegister.HUE, hue / 360f)
            brush.settings.setFloatProperty(PropertyRegister.SATURATION, saturation)
            brush.settings.setFloatProperty(PropertyRegister.LIGHTNESS, lightness)
            brush.settings.setFloatProperty(PropertyRegister.ALPHA, 1)

            set.addBrush(brush)
            brush
        }

        def addBrush2(set: FixedBrushSet, name: String, inks: List[PixelProcessorMetadata], hue: Float, saturation: Float, lightness: Float,
                     radius: Float, tilt: Float, pressureEffectOnRadius: Float): Brush = {
            val brush = new Brush(name, new DataImpl(), inks,
                List(
                    createFilterMetadata(classOf[ZeroLengthSegmentFilter]),
                    createFilterMetadata(classOf[DistanceCalculatorFilter]),
                    createFilterMetadata(classOf[StrokeAngleTilter],("tilt", tilt)),
                    createFilterMetadata(classOf[RadiusFromPressureFilter],("maxRadius", radius), ("pressureEffect", pressureEffectOnRadius)),
                    createFilterMetadata(classOf[StrokeEdgeCalculatorFilter]) ),
                Nil)

            val transparencyInks = new ListenableList(List(
                createInkMetadata(classOf[ColorInk],new DataImpl()),
                createInkMetadata(classOf[AlphaTransparencyBackgroundInk],new DataImpl()) ))
            val hueInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f),
                        ("saturation", 1f),
                        ("lightness", 0.5f))),
                createInkMetadata(classOf[ColorInk] ,new DataImpl()) ))
            val satInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f))),
                createInkMetadata(classOf[ColorInk],new DataImpl()) ))
            val liInk = new ListenableList(List(
                createInkMetadata(classOf[ConstantInk],
                    new DataImpl(
                        ("alpha", 1f))),
                createInkMetadata(classOf[ColorInk],new DataImpl()) ))

            brush.editors.add(new GradientSliderEditor("Hue", "hue", 0, 1, hueInk))
            brush.editors.add(new GradientSliderEditor("Saturation", "saturation", 1, 0, satInk))
            brush.editors.add(new GradientSliderEditor("Lightness", "lightness", 1, 0, liInk))
            brush.editors.add(new GradientSliderEditor("Transparency", "alpha", 1, 0, transparencyInks))
            brush.editors.add(new BrushSliderEditor("Size", "maxRadius", 1, 3 * radius))

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
            currentBrush = addBrush(sketching,"Pencil", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 1)), 2.5f, 0, 0.35f, false, "black")
 //           addFixedSizeBrush(sketching,"Pen", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 0))(solidBlackGradient, 0), 1, 0, "solidBlack")
            addBrush(sketching,"Ink, thin", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 0)), 2.7f, 0.8f, 1, false, "ink")
            addBrush(sketching,"Ink, thick", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 0)), 8, 0.8f, 1, false, "ink")
            addBrush(sketching,"Crayon", createInkMetadata2 (classOf[NoiseInk], ("noiseScaleAcross", 0.3f), ("noiseScaleAlong",3f),("alphaWithDistance", 0.8f), ("octaves", 3)), 20, 0f, 1, true, "charcoal")
            addBrush(sketching,"Rake", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 1)), 17, 0f, 0f, true, "rake")
            addBrush(sketching,"Shade, gray", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 1)), 23, 0, 0.5f, false, "smoothGrey")
            addBrush(sketching,"Shade, black", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 1)), 70, 0, 0.5f, false, "smoothBlack")
            addBrush(sketching,"Shade, white", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 1)), 30, 0, 0.5f, false, "smoothWhite")
            addBrush(sketching,"White", createInkMetadata2 (classOf[GradientInk], ("alphaPressure", 0)), 45, 0, 1f, false, "white")
//            addBrush(sketching,"Spray", createInkMetadata2 (classOf[NoiseInk], ("alphaPressure", 0), ("noiseScaleAcross", 10),  ("noiseScaleAlong",10),("alphaWithDistance", 1f), ("octaves", 3)), 26, 0, 0.5f, false, "spray")
/*
            addBrush2(sketching, "Spray", List(createInkMetadata2 (classOf[NoiseInk],("noiseScaleAcross", 10),  ("noiseScaleAlong",10),("alphaWithDistance", 1f), ("octaves", 3))(sprayGradient , (10f, 10f), 1f, "distance",3),new AlphaFromPressureInk(1), new ColorInk()),0,0.5f,0, 26, 0, 0.5f, "spray")
            addBrush2(sketching, "Dots", List(createInkMetadata2 (classOf[NoiseInk], ("alphaPressure", 1),("noiseScaleAcross", 3.7f),  ("noiseScaleAlong",0.05f), ("octaves", 3), "distance"),new AlphaFromPressureInk(1), new ColorInk()),0,0,0.3f, 55, 0, 0f, "dots")
*/

 /*           val painting = addBrushSet( "Painting" )
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
*/
        /*
            // DEBUG
            val debugBrushes = addBrushSet( "Debuging Brushes" )
            addBrush(debugBrushes, "Dual Debug brush", new DebugInk(1,1), 80, 0, 1)
            addBrush(debugBrushes, "Along Debug brush", new DebugInk(1, 0), 80, 0, 1)
            addBrush(debugBrushes, "Across Debug brush", new DebugInk(0, 1), 80, 0, 1)
            addBrush(debugBrushes, "Solid Debug brush", new DebugInk(0, 0), 40, 0, 1)
        */


        val xml =  FlowPaint.library.toXML

        val FLOWPAINT_FORMAT_VERSION = "1-beta"
        XML.save( "flowpaint-library-out.xml",  <flowpaint version={FLOWPAINT_FORMAT_VERSION}>{xml}</flowpaint> )       

        return (brushSets, currentBrush)
    }


}