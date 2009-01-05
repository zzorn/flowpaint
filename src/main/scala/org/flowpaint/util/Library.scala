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

    override def toString: String = StringUtils.capitalize( identifier.substring( identifier.lastIndexOf( "." ) + 1 ) )
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

    def getTomes[T <: Tome]( tomeType : Class[T]): List[T] = {
      tomes.values.filter( (t : Tome) =>  tomeType.isAssignableFrom( t.getClass() ) ).toList.asInstanceOf[List[T]]
    }
  

    def putTome(tome: Tome) {
        tomes.put(tome.identifier, tome)

    }

    def toXML(): List[Elem] = {
      
      def sortTome(a:Tome,b:Tome):Boolean = {
        val as = a.getClass().getName()
        val bs = b.getClass().getName()
        val typeOrder = as.compareTo( bs )

        if (typeOrder == 0) a.identifier.compareTo(b.identifier) < 0
        else typeOrder < 0
      }

      // Sort the tomes, to get a bit more human readable and workable output
      val tomesSortedByType = tomes.values.toList.sort( sortTome )

      tomesSortedByType map ( _.toXML() )
    }

    def fromXML(elementContainingTomes: Elem) {
        loaders foreach ( _.loadTomes(elementContainingTomes) foreach putTome )
    }



}
