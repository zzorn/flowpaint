package org.flowpaint.util

import _root_.scala.collection.jcl.HashMap
import _root_.scala.xml.{Elem, NodeSeq, Node}
/**
 *
 *
 * @author Hans Haggstrom
 */


trait Tome {
    def identifier: String

    def toXML(): Elem
}


abstract class AbstractTome(identifier_ : String) extends Tome {
    final def identifier = identifier_

}


trait Library {

    def getTome[T <: Tome](identifier: String, default: T): T

    def putTome(tome: Tome)

    def toXML(): List[Elem]

    def fromXML(elementContainingTomes: Elem)

}

case class TomeLoader( tomeTagName : String, tomeReader : (Node) => Tome ) {

  def loadTomes( e: Elem ) : List[Tome] = ((e \ tomeTagName) map tomeReader).toList

}

class LibraryImpl(initialLoaders : TomeLoader* ) extends Library {


    private var loaders = initialLoaders.toList

    private val tomes : HashMap[String, Tome]= new HashMap[String, Tome]()

    def addLoader( loader : TomeLoader ) {
      loaders = loaders ::: List(loader)
    }

    def getTome[T <: Tome](identifier: String, default: T): T = {
        tomes.get(identifier) match {
            case None => default
            case Some(v) => v.asInstanceOf[T]
        }
    }

    def putTome(tome: Tome) {
        tomes.put(tome.identifier, tome)

    }

    def toXML(): List[Elem] = {
        (tomes.values map ( _.toXML() )).toList
    }

    def fromXML(elementContainingTomes: Elem) {
        loaders foreach ( _.loadTomes(elementContainingTomes) foreach putTome )
    }


}
