package org.flowpaint.util


import property.{Data, DataImpl}

/**
 *  Common interface for pixel and path processors.
 */
trait Processor {
    val settings: Data = new DataImpl()

    def init(initialSettings: Data) {
        settings.set(initialSettings)
        onInit()
    }

    protected def onInit() {}

}



/**
 *  Common functionality of pixel and path processor metadata.
 *
 * @author Hans Haggstrom
 */
// TODO: Add support for defining UI widgets for modifying the settings
abstract class ProcessorMetadata[T <: Processor](processorType: Class[_ <: T], initialSettings : Data) {

    val settings = new DataImpl( initialSettings )

    def createProcessor(): T = {

        val processor: T = processorType.newInstance

        processor.init(settings)

        processor
    }


}

