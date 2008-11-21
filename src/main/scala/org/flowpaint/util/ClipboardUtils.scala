package org.flowpaint.util

import java.awt.datatransfer.{Transferable, Clipboard, StringSelection, ClipboardOwner}
import java.awt.datatransfer.DataFlavor
/**
 *
 *
 * @author Hans Haggstrom
 */

object ClipboardUtils {

  /**
   * Returns the system clipboard
   */
  def getClipboard() : Clipboard = java.awt.Toolkit.getDefaultToolkit.getSystemClipboard()

  /**
   * Checks if the clipboard is available and working.
   */
  def isCliboardAvailable(): Boolean = {
    try {
      // Check access with security manager
      val sm = System.getSecurityManager()
      if (sm != null) sm.checkSystemClipboardAccess()

      // Try to get the current data
      getClipboard.getContents()
    }
    catch {
      // Some problem, clipboard not available
      case e: Exception => return false
    }

    // Everything seems to be ok
    return true
  }

  /**
   * Copies the specified text to the clipboard if possible.  May throw an error if the clipboard is not available.
   */
  def setClipboardContents(text: String) {
    val selection = new StringSelection(text)

    getClipboard.setContents( selection, new ClipboardOwner() {
      def lostOwnership(clipboard : Clipboard , transferable : Transferable ) {
        // Someone else copied something to the clipboard?
      }
    } )
  }

  /**
   * Returns the content of the clipboard as a string, or null if it is empty or no string is available
   */
  def getClipboardContents(): String= {
    try {
      val transferable = getClipboard.getContents( null )
      val hasTransferableText = (transferable != null) && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)

      if (hasTransferableText) transferable.getTransferData(DataFlavor.stringFlavor).asInstanceOf[String] else null
    }
    catch {
      case e : Exception => return null
    }

  }

}