package quoridor.control;

import boardifier.control.Controller;
import boardifier.control.ControllerKey;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/**
 * The QuoridorControllerKey class represents a controller key specific to the game Quoridor.
 * It extends the ControllerKey class and implements the EventHandler interface for handling KeyEvents.
 *
 * @see ControllerKey
 * @see EventHandler
 */
public class QuoridorControllerKey extends ControllerKey implements EventHandler<KeyEvent> {
    public QuoridorControllerKey(Model model, View view, Controller control) {
        super(model, view, control);
    }

    /**
     * Handles the KeyEvent.
     * @param event the KeyEvent to handle
     */
    public void handle(KeyEvent event) {
        if (!model.isCaptureKeyEvent()) return;

//        System.out.println(event.getCode());
    }
}
