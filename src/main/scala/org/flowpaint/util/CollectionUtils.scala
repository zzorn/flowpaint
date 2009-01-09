package org.flowpaint.util
import _root_.scala.collection._

/**
 * 
 *
 * @author Hans Haggstrom
 */

object CollectionUtils {

  def listToIndexMap[T]( list : List[T] ) : Map[T, Int] = {

    var map : immutable.Map[T, Int] = new immutable.HashMap[T, Int]()

    var i = 0
    list foreach ( (item : T ) => {
      map = ( map( item ) = i )
      i += 1
    })

    map

  }

}

