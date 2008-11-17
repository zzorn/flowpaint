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
  val VERSION = "0.1"
  val VERSION_DESC = "Alpha"
  val RELEASE_DATE = "November 2008"

  val DESCRIPTION = APPLICATION_NAME + " v. " + VERSION + " " + VERSION_DESC
  val FULL_DESCRIPTION = DESCRIPTION + ", released " + RELEASE_DATE

  // TODO: Move to some resource file?
  val ABOUT = "<html>" +
          "<p><center><b>" + FlowPaint.FULL_DESCRIPTION + "</b></center></p>" +
          "<br/>" +
          "<p>FlowPaint intends to be an intuitive and powerful cross-platform next generation paint program.</p>" +
          "<br/>" +
          "<p>It is currently at an early stage of development, with basic brush rendering and a rudimentary user interface.</p>" +
          "<br/>" +
          "<p>What is planned for the final version:</p>" +
          "<ul>" +
          "<li>A user friendly on-screen docked UI, giving easy access to often and recently used tools, <br/>" +
          "settings and pictures, with a possibility to toggle to full screen mode when needed.</li>" +
          "<li>Possibility to create own brushes from textures or procedural functions, and control them <br/>" +
          "using pen-pressure or on-screen sliders.</li>" +
          "<li>An infinite canvas, with easy navigation and the possibility to rescale a picture to any resolution.</li>" +
          "<li>Wrapping canvas mode for creating seamlessly tiling textures.</li>" +
          "<li>Integrated downloading and sharing of brushes and other settings online, with a built in <br/>" +
          "rating and tagging system to enable you to quickly find the right tool for a job.</li>" +
          "</ul>" +
          "<br/>" +
          "<center><i>www.flowpaint.org</i></center><br/>" +
          "<center>Programming by Hans Häggström ( zzorn at iki.fi )</center><br/>" +
          "<center>Licensed under GPL v2</center><br/>" +
          "<br/>" +
          "</html>"

  def main(args: Array[String]) {


    println("FlowPaint started.")


    // TODO: Better structuring of the main application classes needed.
    FlowPaintController.start()

  }

}
