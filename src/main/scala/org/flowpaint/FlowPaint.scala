package org.flowpaint

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Dimension, BorderLayout}
import javax.swing._
import model.{Stroke, Painting}
import renderer.SingleRenderSurface
import util.DataSample

/**
 *  Main entrypoint for FlowPaint.
 */
object FlowPaint {

  // TODO: Move these to some property file?
  val APPLICATION_NAME = "FlowPaint"
  val VERSION = "0.1"
  val RELEASE_DATE = "in November 2008"
  val HOMEPAGE_PRETTY = "www.flowpaint.org"
  val TAG_LINE= "FlowPaint aims to be an intuitive and powerful next generation paint program."
  val CREDITS = "Programmed by Hans Häggström ( zzorn @ iki.fi )"
  val LICENSE = "GPL v2"


  val HOMEPAGE_URL = "http://" + HOMEPAGE_PRETTY
  val NAME_AND_VERSION = APPLICATION_NAME + " " + VERSION
  val NAME_VERSION_AND_DATE = APPLICATION_NAME + " v. " + VERSION + ", released " + RELEASE_DATE

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
