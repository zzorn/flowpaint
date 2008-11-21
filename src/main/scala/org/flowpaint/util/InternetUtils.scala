package org.flowpaint.util

import edu.stanford.ejalbert.BrowserLauncher
import edu.stanford.ejalbert.exception.{UnsupportedOperatingSystemException, BrowserLaunchingInitializingException, BrowserLaunchingExecutionException}
import java.net.URL
import javax.swing.{JDialog, JOptionPane, JTextField}

/**
 * 
 * 
 * @author Hans Haggstrom
 */

object InternetUtils {


  def openUrlInBrowser( url : String, description : String ) {

    def showCopyUrlWindow(message : String){

      // Well, this is going pretty far, but lets check if the clipboard is available too..
      // Who knows how well it works from java on various linux flavours
      val clipboardAvailable = util.ClipboardUtils.isCliboardAvailable

      val field = new JTextField(url)
      field.setEditable(false)
      field.setBorder(null)

      var alternatives :Array[Object]= null
      var instructions: String= null
      var options: Int = 0
      if (!clipboardAvailable) {
        field.selectAll()
        alternatives = Array( "Ok" )
        instructions = "Copy the URL from the field below, \n" +
              "then paste it in your browsers address field."
        options = JOptionPane.OK_OPTION
      }
      else  {
        alternatives = Array( "Copy URL","Cancel" )
        instructions = "Copy the URL with the button below, \n" +
              "then paste it in your browsers address field."
        options = JOptionPane.OK_CANCEL_OPTION
      }
      
      val messages :Array[Object]= Array(
        message,
        instructions,
        field )

      val selectedValue = JOptionPane.showOptionDialog(
        FlowPaintUi.frame,
        messages,
        "Copy URL to Browser",
        options,
        JOptionPane.PLAIN_MESSAGE,
        null,
        alternatives,
        alternatives(0))


      if ( clipboardAvailable && selectedValue == JOptionPane.OK_OPTION)
        util.ClipboardUtils.setClipboardContents( url )
    }

    val launcher = new BrowserLauncher()

    try {
      launcher.openURLinBrowser(url);
    }
    catch {
      case e : UnsupportedOperatingSystemException => showCopyUrlWindow( "Could not open the "+description+" in a browser directly on this operating system."  )
      case e: BrowserLaunchingExecutionException =>showCopyUrlWindow( "Could not open the "+description+" in a browser directly."  )
      case e : BrowserLaunchingInitializingException =>showCopyUrlWindow( "Could not open the "+description+" in a browser directly."  )
    }
  }


}