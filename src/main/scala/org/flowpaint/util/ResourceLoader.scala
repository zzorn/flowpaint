package org.flowpaint.util

import _root_.scala.xml.Elem
import java.awt.image.BufferedImage
import java.io.{InputStreamReader, InputStream, Reader}
import java.util.Properties
import javax.imageio.ImageIO
import javax.swing.ImageIcon

/**
 *
 *
 * @author Hans Haggstrom
 */

object ResourceLoader {
    def loadImage(resourcePath: String): BufferedImage = loadImage(resourcePath, null)

    def loadImage(resourcePath: String, placeholder: BufferedImage): BufferedImage = {
        loadResource[BufferedImage](resourcePath, "BufferedImage", ImageIO.read, placeholder)
    }

    def loadIcon(resourcePath: String): ImageIcon = {
        new ImageIcon(loadImage(resourcePath))
    }

    def loadProperties(resourcePath: String): Properties = {
        val properties = new Properties()

        loadResource[Properties](resourcePath, "Properties", (stream) => {
            properties.load(new InputStreamReader(stream))
            properties
        }, properties)
    }

    def loadXml(resourcePath: String, resourceDesc: String): Elem = {
        loadXml(resourcePath, resourceDesc, null)
    }

    def loadXml(resourcePath: String, resourceDesc: String, default: Elem): Elem = {
        loadResource[Elem](resourcePath, resourceDesc, scala.xml.XML.load, default)
    }

    def loadResource[T](resourcePath: String, resourceDesc: String, loader: (InputStream) => T, default: => T): T = {
        // TODO: Could return a future maybe - requires a spawn type future support for scala (scalax.Future seems promising)
        // But then it should probably get the concrete default object.

        try
        {
            val classloader: ClassLoader = ResourceLoader.getClass().getClassLoader()
            return loader(classloader.getResourceAsStream(resourcePath))
        }
        catch
        {
            case e: Exception => {
                val defaultVal = default

                // TODO: Use logging
                System.err.println("Failed to load the resource '" + resourceDesc + "' " +
                        "from the location '" + resourcePath + "' on the classpath" +
                        (if (defaultVal != null) " (using default value instead)" else "") + " : " + e.toString())
                e.printStackTrace

                return defaultVal
            }
        }
    }


}