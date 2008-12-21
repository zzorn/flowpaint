package org.flowpaint.util
import _root_.scala.collection.jcl.HashMap

/**
 * 
 * 
 * @author Hans Haggstrom
 */

trait Library {

    def getTome[T]( reference : String, default : T ) : T

    def putTome( reference : String, tome : Object )

}


class LibraryImpl extends Library {

  val tomes = new HashMap[String, Object]()


    def getTome[T]( reference : String, default : T ) : T = {
        tomes.get(reference) match {
            case None => default
            case Some(v) => v.asInstanceOf[T]
        }
    }

    def putTome( reference : String, tome : Object ) {
        tomes.put(reference, tome)

    }
}
