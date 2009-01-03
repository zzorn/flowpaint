package org.flowpaint.pixelprocessors;

/**
 * @author Hans Haggstrom
 */
public interface PixelProgram
{
    /**
     * Calculates the output value for a pixel based on the specified input values.
     *
     * @param inputs the inputs expected by this PixelProgram.
     * @param outputs an array to write the outputs to.
     */
    void calculatePixelValues( float[] inputs, float[] outputs );
}
