package org.flowpaint.pixelprocessors

import _root_.org.flowpaint.property.{Data, DataImpl}
import _root_.scala.collection.Map
import org.flowpaint.gradient.GradientPoint
import org.flowpaint.pixelprocessor.PixelProcessor
import org.flowpaint.util.{DataSample, PropertyRegister}
import org.flowpaint.FlowPaint

/**
 * 
 * 
 * @author Hans Haggstrom
 */
class Gradient extends PixelProcessor( "","","") {

    override def generateCode(variableNames: List[String],
                    nameToIndex: Map[String, Int],
                    generalSettings: Data,
                    variableArrayName: String,
                    nextUniqueId: () => Int): (String, String, String) = {

      val uniqueId = nextUniqueId()

      val gradientIdentifier = getMappedString( "gradient", null, generalSettings )
      val gradient : org.flowpaint.gradient.Gradient = if (gradientIdentifier == null) null else FlowPaint.library.getTome[org.flowpaint.gradient.Gradient]( gradientIdentifier, null )

      if (gradient != null) {

        val gradientPoints : List[GradientPoint] = gradient.getPoints
        val gradientProperties : List[String] = (gradientPoints flatMap ( gradPoint => gradPoint.data.getFloatPropertyNames() )).removeDuplicates
        val gradientLength = gradient.getPoints.size

        def gradientName(property :String) = "gradient_" + PropertyRegister.getId(property) + "_values_" + uniqueId
        def gradientOffsetName() = "gradientoffsets_" + uniqueId


        def makeFloatArray( s : StringBuilder, nameFunc : () => String, valueFunc : (GradientPoint) => Float ) {
          s append "private static final float[] "
          s append ( nameFunc() + " = { " )

          gradientPoints foreach (( gradientPoint : GradientPoint ) => {
            s append (  valueFunc( gradientPoint ) + "f, " )
          })

          s append " }; "
          s append "\n"
        }

        val gradientClassBodyTemplate = new StringBuilder()

        gradientProperties foreach ( (propName : String) => {

          makeFloatArray( gradientClassBodyTemplate,
                          () => gradientName(propName),
                          (g : GradientPoint) =>  g.data.getFloatProperty( propName, 0f ) )
        })

        makeFloatArray( gradientClassBodyTemplate,
                        () => gradientOffsetName(),
                        (g : GradientPoint) =>  g.position )


        gradientClassBodyTemplate append
          """
            private static int binarySearch_gradient$id$(float key) {
                int first = 0;
                int upto = """+gradientLength+""";
                while (first < upto) {
                    // Compute mid point.
                    int mid = (first + upto) / 2;
                    if (key < """+gradientOffsetName()+"""[mid]) {
                        // repeat search in bottom half.
                        upto = mid;
                    } else if (key > """+gradientOffsetName()+"""[mid]) {
                        // Repeat search in top half.
                        first = mid + 1;
                    } else {
                        // Found it. return position
                        return mid;
                    }
                }
                // Failed to find key, return -insertion position -1
                return -(first + 1);
            }
          """


        val gradientLoopedTemplate = new StringBuilder()
        gradientLoopedTemplate append
        """

          float gradientPosition$id$ = $getScaleOffsetFloat gradientPosition, 0.5f$;

          // Clamp
          if ( gradientPosition$id$ < 0f) gradientPosition$id$ = 0f;
          if ( gradientPosition$id$ > 1f) gradientPosition$id$ = 1f;

        """

        def generateCodeForCopyValuesFromIndex( index : String ) : String = {
          val s = new StringBuilder()
          gradientProperties foreach ( (propName : String) => {
            s append ("$setFloat "+propName+"$ = " + gradientName(propName) + "[ "+index+" ];\n")
          })
          s.toString
        }

        def generateCodeForCopyValuesFromInterpolatedIndex( lastIndex : String, interpolation : String, invertedInterpolation : String  ) : String =  {
          val s = new StringBuilder()
          gradientProperties foreach ( (propName : String) => {
            s append ("$setFloat "+propName+"$ = " + gradientName(propName) + "[ "+lastIndex+" - 1 ] * "+invertedInterpolation+" + "+
                                           gradientName(propName) + "[ "+lastIndex+" ] * "+interpolation+"; \n")
          })
          s.toString
        }


          if ( gradientLength == 0) {
            gradientLoopedTemplate append "\n"
          }
          else if ( gradientLength == 1) {
            gradientLoopedTemplate append generateCodeForCopyValuesFromIndex( " 0 " )
          }
          else {

            gradientLoopedTemplate append
              """

                int gradientPointIndex$id$ = binarySearch_gradient$id$(gradientPosition$id$);
                if (gradientPointIndex$id$ >= 0)
                    {
                        // Found exact value match, use values of that point
                        """+generateCodeForCopyValuesFromIndex( "gradientPointIndex$id$" )+"""
                    }
                else
                    {
                        // Search returns -insertionPoint - 1 if the exact value was not found,
                        // calculate the insertion point
                        int insertionPointIndex$id$ = -(gradientPointIndex$id$ + 1);

                        if (insertionPointIndex$id$ >= """+gradientLength+ """)
                            {
                                // Last point is closest
                                """+generateCodeForCopyValuesFromIndex( " " + (gradientLength -1) + " " )+"""
                            }
                        else if (insertionPointIndex$id$ <= 0)
                            {
                                // First color is closest
                                """+generateCodeForCopyValuesFromIndex( " 0 " )+"""
                            }
                        else
                            {
                                float gradientStartValue$id$ = """ +gradientOffsetName()+ """[insertionPointIndex$id$ - 1];
                                float gradientEndValue$id$   = """ +gradientOffsetName()+ """[insertionPointIndex$id$];

                                if ( gradientStartValue$id$ == gradientEndValue$id$ )
                                    {
                                        """+generateCodeForCopyValuesFromIndex( " insertionPointIndex$id$ - 1 " )+"""
                                    }
                                else
                                    {
                                        float gradientRelativePosition$id$ = (gradientPosition$id$ - gradientStartValue$id$) / (gradientEndValue$id$ - gradientStartValue$id$);
                                        float gradientRelativePositionInverted$id$ = 1f - gradientRelativePosition$id$;
                                        """ +generateCodeForCopyValuesFromInterpolatedIndex( " insertionPointIndex$id$ ", "gradientRelativePosition$id$", "gradientRelativePositionInverted$id$" )+ """
                                    }
                            }
                    }
              """

          }



        ( parseTemplate(nameToIndex, gradientClassBodyTemplate.toString, generalSettings, variableArrayName, uniqueId),
          "",
          parseTemplate(nameToIndex, gradientLoopedTemplate.toString, generalSettings, variableArrayName, uniqueId) )
      }
      else ( "", "", "" )
    }



  

}