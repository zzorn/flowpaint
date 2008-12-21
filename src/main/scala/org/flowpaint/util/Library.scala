package org.flowpaint.util
import _root_.scala.collection.jcl.HashMap

/**
 * 
 * 
 * @author Hans Haggstrom
 */


trait Tome {

  def identifier : String

}


abstract class AbstractTome(identifier_ : String) extends Tome{

  final def identifier = identifier_

}



trait Library {

    def getTome[T <: Tome ]( identifier : String, default : T ) : T

    def putTome( tome : Tome )

}


class LibraryImpl extends Library {

  val tomes = new HashMap[String, Tome]()


    def getTome[T <: Tome ]( identifier : String, default : T ) : T = {
        tomes.get(identifier) match {
            case None => default
            case Some(v) => v.asInstanceOf[T]
        }
    }

    def putTome( tome : Tome ) {
        tomes.put( tome.identifier, tome)

    }
}
