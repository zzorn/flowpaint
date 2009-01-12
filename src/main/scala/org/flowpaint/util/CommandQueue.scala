package org.flowpaint.util

import java.awt.event.{ActionEvent, KeyEvent}
import javax.swing.{KeyStroke, AbstractAction, Action}

case class Command(description: String,
                  action: () => Object,
                  undoAction: (Object) => Object,
                  redoAction: (Object) => Object,
                  canUndo : () => Boolean,
                  clearUndoQueue: Boolean) {
    def this(description: String,
            action: () => Object,
            undoAction: (Object) => Object,
            redoAction: (Object) => Object,
            canUndo : () => Boolean) {
        this (description, action, undoAction, redoAction, canUndo, false)
    }

    def this(description: String,
            action: () => Object,
            undoAction: (Object) => Object) {
        this (description, action, undoAction, null, null, false)
    }

    def this(description: String,
            action: () => Unit) {
        this (description, () => {action(); null}, null, null, null, false)
    }
}


/**
 * A command queue object, for keeping undo and redo queues.
 *
 * @author Hans Haggstrom
 */
class CommandQueue {
    private var undoQueue: List[(Command, Object)] = Nil
    private var redoQueue: List[(Command, Object)] = Nil
    private var commandQueue: List[Command] = Nil

    private var listeners: List[()=>Unit] = Nil

    def addListener( listener : ()=>Unit ) : Unit = listeners = listener :: listeners
    private def notifyListeners() : Unit = listeners foreach ( _() )

    def queueCommand(command: Command) {

        // TODO: Could implement different running strategies, e.g. run in separate worker thread.  But it could cause problems if swing is accessed.

        if (command.clearUndoQueue) {
            undoQueue = Nil
            redoQueue = Nil
        }

        if (command != redoCommand && command != undoCommand ) redoQueue = Nil

        runCommand( command )
    }

    private def runCommand( command : Command ) {
        val undoData = command.action()

        if (command.undoAction != null)
            undoQueue = (command, undoData) :: undoQueue

        notifyListeners()
    }

    val undoCommand = new Command("Undo", () => {undo} )
    val redoCommand = new Command("Redo", () => {redo})

    private def updateStatus( action : Action) {
        action.setEnabled( canUndo )
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

            val redoData = command.undoAction( undoData )

            redoQueue = (command, redoData) :: redoQueue

            notifyListeners()
        }
    }

    def redo() {
        if (canRedo) {

            val (command, redoData) = redoQueue.head
            redoQueue = redoQueue.tail

            val undoData = if (command.redoAction != null) command.redoAction( redoData )
                           else command.action()

            undoQueue = (command, undoData) :: undoQueue

            notifyListeners()
        }
    }

    def canUndo(): Boolean = !undoQueue.isEmpty && undoQueue.head._1.canUndo()

    def canRedo(): Boolean = !redoQueue.isEmpty

}



