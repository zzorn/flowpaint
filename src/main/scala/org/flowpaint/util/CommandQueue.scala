package org.flowpaint.util

import java.awt.event.{ActionEvent, KeyEvent}
import javax.swing.{KeyStroke, AbstractAction, Action}

/**
 * T is the document the command should be applied to.
 */
case class Command[T](description: String,
                  action: (T) => Object,
                  undoAction: (T, Object) => Object,
                  redoAction: (T, Object) => Object,
                  canUndo : (T) => Boolean,
                  clearUndoQueue: Boolean) {
    def this(description: String,
            action: (T) => Object,
            undoAction: (T, Object) => Object,
            redoAction: (T, Object) => Object,
            canUndo : (T) => Boolean) {
        this (description, action, undoAction, redoAction, canUndo, false)
    }

    def this(description: String,
            action: (T) => Object,
            undoAction: (T, Object) => Object) {
        this (description, action, undoAction, null, null, false)
    }

    def this(description: String,
            action: (T) => Unit) {
        this (description, (x: T) => {action(x); null}, null, null, null, false)
    }
}


/**
 * A command queue object, for keeping undo and redo queues.
 *
 * @author Hans Haggstrom
 */
class CommandQueue[T](document: T) {

    private var undoQueue: List[(Command[T], Object)] = Nil
    private var redoQueue: List[(Command[T], Object)] = Nil
    private var commandQueue: List[Command[T]] = Nil

    private var listeners: List[()=>Unit] = Nil

    def addListener( listener : ()=>Unit ) : Unit = listeners = listener :: listeners
    private def notifyListeners() : Unit = listeners foreach ( _() )

    def queueCommand(command: Command[T]) {

        // TODO: Could implement different running strategies, e.g. run in separate worker thread.  But it could cause problems if swing is accessed.

        if (command.clearUndoQueue) {
            undoQueue = Nil
            redoQueue = Nil
        }

        if (command != redoCommand && command != undoCommand ) redoQueue = Nil

        runCommand(command)
    }

    private def runCommand(command : Command[T]) {
        val undoData = command.action(document)

        if (command.undoAction != null)
            undoQueue = (command, undoData) :: undoQueue

        notifyListeners()
    }

    val undoCommand = new Command[T]("Undo", () => {undo()})
    val redoCommand = new Command[T]("Redo", () => {redo()})

    private def updateStatus(action : Action) {
        action.setEnabled(canUndo)
    }

    val undoAction: Action = new AbstractAction("Undo") {
        putValue(Action.SHORT_DESCRIPTION, "Undo the last operation")
        putValue(Action.LONG_DESCRIPTION, "Undo the last operation")
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_U)
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_DOWN_MASK))

        updateStatus(this)

        addListener( () => {updateStatus(this)} )

/*
        override def isEnabled = {
            canUndo
        }
*/

        def actionPerformed(e: ActionEvent) = {
            queueCommand(undoCommand)
        }
    }


    val redoAction: Action = new AbstractAction("Redo") {
        putValue(Action.SHORT_DESCRIPTION, "Redo the last undoed operation")
        putValue(Action.LONG_DESCRIPTION, "Redo the last undoed operation")
        putValue(Action.MNEMONIC_KEY, KeyEvent.VK_R)
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_DOWN_MASK))

        updateStatus(this)
        
        addListener( () => {updateStatus(this)} )

/*
        override def isEnabled = {
            canRedo
        }
*/

        def actionPerformed(e: ActionEvent) = {
            queueCommand(redoCommand)
        }
    }


    def undo() {
        if (canUndo) {

            val (command, undoData) = undoQueue.head
            undoQueue = undoQueue.tail

            val redoData = command.undoAction(document, undoData)

            redoQueue = (command, redoData) :: redoQueue

            notifyListeners()
        }
    }

    def redo() {
        if (canRedo) {

            val (command, redoData) = redoQueue.head
            redoQueue = redoQueue.tail

            val undoData = if (command.redoAction != null) command.redoAction(document, redoData)
                           else command.action(document)

            undoQueue = (command, undoData) :: undoQueue

            notifyListeners()
        }
    }

    def canUndo(): Boolean = !undoQueue.isEmpty && undoQueue.head._1.canUndo(document)

    def canRedo(): Boolean = !redoQueue.isEmpty

}



