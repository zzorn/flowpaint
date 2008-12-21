package org.flowpaint.util

import _root_.scala.collection.mutable.HashSet
import brush.Brush

/**
 *    Encapsulates a list that can be added and removed from, and that notifies specified listeners when items are added or removed.
 *
 *    Throws illegal argument exception if nulls are attempted to be added.
 *
 * @author Hans Haggstrom
 */
class ListenableList[T](initialList: List[T], additionListener: (ListenableList[T], T) => Unit, removalListener: (ListenableList[T], T) => Unit) {

    /**
     *   Creates a list with a single listener that is notified when elements are added or removed.
     */
    def this(initialList: List[T], changeListener: (ListenableList[T]) => Unit) {
        this (initialList,
            (list : ListenableList[T], added: T) => {changeListener(list)},
            (list : ListenableList[T], removed: T) => {changeListener(list)})
    }

    /**
     *   Creates a simple list with add and remove functions, without listeners.
     */
    def this(initialList: List[T]) {
        this (initialList, null, null)
    }

    /**
     *   Creates a simple list with add and remove functions, without listeners or initial elements.
     */
    def this() {
        this (Nil, null, null)
    }


    private var elementList: List[T] = if (initialList == null) Nil else initialList


    /**
     *   The current elements in the list.
     */
    def elements = elementList

    /**
     *    Add an element to the end of the list.  The element should not be null.
     */
    def add(element: T) {
        if (element == null) throw new IllegalArgumentException("Null element not allowed in list.")

        elementList = elementList ::: List(element)

        if (additionListener != null) additionListener(this, element)
    }


    /**
     *    Removes an element from the list.
     */
    def remove(element: T) {
        if (element != null)
            {
                var atLeastOneFound = false
                elementList = elementList.remove((e: T) => {
                    val found = e == element
                    atLeastOneFound = atLeastOneFound || found
                    found
                })

                if (atLeastOneFound && removalListener != null) removalListener(this, element)
            }
    }


}