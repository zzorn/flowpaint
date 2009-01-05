package org.flowpaint.pixelprocessor;

/**
 * Interface for on-the-fly generated PixelProgram:s
 * 
 * @author Hans Haggstrom
 */
public interface PixelProgram
{
    /**
     * Calculates the output value for a pixel based on the specified input values.
     *
     * @param variables the variables used by this PixelProgram.  Some may be initialized as input variables.
     * @param outputOffset the offset to write the output variables at in the outputArray.
     * @param outputArray an array to write the outputs to.
     */
    void calculatePixelValues( float[] variables, int outputOffset, float[] outputArray );
}
