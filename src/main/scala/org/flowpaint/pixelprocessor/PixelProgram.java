package org.flowpaint.pixelprocessor;

/**
 * Interface for on-the-fly generated PixelProgram:s
 * 
 * @author Hans Haggstrom
 */
public interface PixelProgram
{
    /**
     * Calculates the pixel values for a scanline.
     */
    void calculateScanline( float[] variablesAtScanlineStart,
                            float[] variablesAtScanlineEnhd,
                            float[] workingArray,
                            int[] destinationBuffer,
                            int startOffset,
                            int scanlineLength );
}
