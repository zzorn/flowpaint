package org.flowpaint.tools

import org.flowpaint.util.DataSample

/**
 * Something that recieves events from the input devices.
 *
 * @author Hans Haggstrom
 */

trait Tool {

    /**
     * Called when there is input.
     *
     * @param event            an event from the input devices.  Can contain one or more variables that
     *                         describe its state.
     * @param controller the {@link FlowPaintController }.  Can be used to access the view and other parts
     *                         of the program.
     */
    def onEvent( event : DataSample )

}