package org.flowpaint

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import java.io.{IOException, Reader}
import java.util.Properties
import javax.swing._
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.{DataSample, ResourceLoader}
/**
 *  Main entrypoint for FlowPaint.
 */
object FlowPaint {


  // Get often changing and potentially changing data from property file
  // (things like version numbers and build numbers)
  val properties  =  ResourceLoader.loadProperties("application.properties")

  def loadIcon( name : String ) : ImageIcon = ResourceLoader.loadIcon("images/"+name)

  val APPLICATION_ICON = loadIcon( "logo-swirl-transparent.png" )


  val STATUS = properties.getProperty("status", "")
  val APPLICATION_NAME = properties.getProperty("applicationName", "Flowpaint")
  val VERSION = properties.getProperty("version", "N/A")
  val VERSION_AND_STATUS = VERSION + " " + STATUS
  val REPOSITORY_VERSION = properties.getProperty("repositoryVersion", "N/A")
  val RELEASE_DATE = properties.getProperty("releaseDate", "N/A")
  val HOMEPAGE_PRETTY = properties.getProperty("homepage", "www.flowpaint.org")
  val TAG_LINE= properties.getProperty("oneLineDescription", "FlowPaint aims to be an intuitive and powerful next generation paint program.")
  val CREDITS = properties.getProperty("credits", "Programmed by Hans Häggström ( zzorn @ iki.fi )")
  val LICENSE = properties.getProperty("license", "GPL v2")
  val BUG_REPORT_URL = properties.getProperty("bugReportUrl", "http://code.google.com/p/flowpaint/issues/entry?template=Defect%20report%20from%20user")
  val FEATURE_REQUEST_URL = properties.getProperty("featureRequestUrl", "http://code.google.com/p/flowpaint/issues/entry?template=Feature%20request%20from%20user")



  val HOMEPAGE_URL = "http://" + HOMEPAGE_PRETTY
  val NAME_AND_VERSION = APPLICATION_NAME + " " + VERSION_AND_STATUS
  val NAME_VERSION_AND_DATE = APPLICATION_NAME + " v. " + VERSION_AND_STATUS  + " (revision "+REPOSITORY_VERSION+"), released " + RELEASE_DATE

  val ABOUT = "<html>" +
          "<p><center><b>" + NAME_VERSION_AND_DATE + "</b></center></p>" +
          "<p></p>"+
          "<p>"+TAG_LINE+"</p>" +
          "<p></p>"+
          "<center>"+HOMEPAGE_PRETTY+"</center>" +
          "<p></p>"+
          "<center>"+CREDITS+"</center>" +
          "<center>Licensed under "+LICENSE+"</center>" +
          "<p> </p>"+
          "<p> </p>"+
          "</html>"

  def main(args: Array[String]) {

    println( APPLICATION_NAME+ " started.")

    // TODO: Better structuring of the main application classes needed.
    FlowPaintController.start()
  }

}
