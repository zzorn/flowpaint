package org.flowpaint.model

import scala.collection.mutable.HashMap
import scala.collection.{mutable, immutable}

/**
 *  Keep track of the registered stroke properties.  Provides a name to id mapping service.
 *
 * @author Hans Haggstrom
 */
object StrokePropertyRegister {

  private val idToName = new HashMap[Int, String]()
  private val nameToId = new HashMap[String, Int]()

  /**
   * @param name the name to get or create an id for.
   *
   * @return the id for the specified variable name.  If none is registered before, a new one is registered
   *          and returned.
   */
  def getId(name: String): Int = {

    nameToId.get(name) match {

      case Some(id) => id

      case None => {
        val id = idToName.size

        idToName.put(id, name)
        nameToId.put(name, id)

        id
      }
    }
  }


  /**
   * @param id the id to get a name for.
   *
   * @return the name of the variable with the specified id, or null if none registered.
   */
  def getName(id: Int): String = idToName(id)


  /**
   *  Clears the registry from all registered id:s.  Used in unit testing.
   */
  def clear = {
    idToName.clear
    nameToId.clear
  }


}