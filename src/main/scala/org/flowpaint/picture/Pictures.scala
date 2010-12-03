package org.flowpaint.picture

import java.io.File
import org.flowpaint.model2.Picture

/**
 * Keeps track of the currently open pictures.
 */
class Pictures {

  type PictureSetListener = Pictures => Unit
  type CurrentPictureListener = Picture => Unit
  private var pictures: List[Picture]  = Nil
  private var _currentPicture: Picture = null
  private var pictureSetChangedListeners: List[PictureSetListener] = Nil
  private var currentPictureListeners: List[CurrentPictureListener] = Nil

  def addPictureSetListener(listener: PictureSetListener) = pictureSetChangedListeners ::= listener
  def removePictureSetListener(listener: PictureSetListener) = pictureSetChangedListeners -= listener

  def addCurrentPictureListener(listener: CurrentPictureListener) = currentPictureListeners ::= listener
  def removeCurrentPictureListener(listener: CurrentPictureListener) = currentPictureListeners -= listener

  def newPicture() {
    val picture = new Picture()
    addPicture(picture)
    setCurrentPicture(picture)
  }

  def loadPicture(file: File) {
    // TODO
  }

  // TODO: Add picture format object with format specific settings
  def saveCurrentPicture(file: File, format: String) {
    // TODO
  }

  def closePicture(picture: Picture) {
    removePicture(picture)
  }

  def currentPicture: Picture = _currentPicture

  /** Changes current picture to the next one */
  def nextPicture() {
    if (pictures.size > 0) {
      val index = pictures.indexOf(currentPicture)
      setCurrentPicture(pictures((index + 1) % pictures.size))
    }
    else setCurrentPicture(null)
  }

  /** Changes current picture to the previous one */
  def prevPicture() {
    if (pictures.size > 0) {
      val index = pictures.indexOf(currentPicture)
      setCurrentPicture(pictures((pictures.size + index - 1) % pictures.size))
    }
    else setCurrentPicture(null)
  }

  def setCurrentPicture(picture: Picture) {
    if (picture != _currentPicture) {
      if (picture != null && !pictures.contains(picture)) addPicture(picture)
      _currentPicture = picture
      currentPictureListeners foreach (_(picture))
    }
  }

  private def addPicture(picture: Picture) {
    require(picture != null)
    require(!pictures.contains(picture))

    pictures ::= picture
    pictureSetChangedListeners foreach (_(this))
  }

  private def removePicture(picture: Picture) {
    require(picture != null)
    require(pictures.contains(picture))

    pictures -= picture

    if (picture == _currentPicture) nextPicture()

    pictureSetChangedListeners foreach (_(this))
  }


}