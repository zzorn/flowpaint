package org.flowpaint

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import javax.swing._
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.DataSample

/**
 *  Main entrypoint for FlowPaint.
 *
 */
object FlowPaint {
  val APPLICATION_NAME = "FlowPaint"
  val VERSION = "0.1.0"
  val RELEASE_DATE = "November 2008"
  val HOMEPAGE_PRETTY = "www.flowpaint.org"
  val HOMEPAGE_URL = "http://" + HOMEPAGE_PRETTY

  val NAME_AND_VERSION = APPLICATION_NAME + " v. " + VERSION
  val NAME_VERSION_AND_DATE = NAME_AND_VERSION + ", released " + RELEASE_DATE

  // TODO: Move to some resource file?
  val ABOUT = "<html>" +
          "<p><center><b>" + NAME_VERSION_AND_DATE + "</b></center></p>" +
          "<p></p>"+
          "<p>FlowPaint aims to be an intuitive and powerful cross-platform next generation paint program.</p>" +
          "<p></p>"+
          "<center>"+HOMEPAGE_PRETTY+"</center>" +
          "<p></p>"+
          "<center>Programmed by Hans Häggström ( zzorn @ iki.fi )</center>" +
          "<center>Licensed under GPL v2</center>" +
          "<p> </p>"+
          "<p> </p>"+
          "</html>"

  def main(args: Array[String]) {


    println("FlowPaint started.")


    // TODO: Better structuring of the main application classes needed.
    FlowPaintController.start()

  }

}
