package quoridor.control;

import boardifier.control.Controller;
import boardifier.control.ControllerAction;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import quoridor.view.QuoridorView;

/**
 * The QuoridorControllerAction class represents a controller action specific to the game Quoridor.
 * It extends the ControllerAction class and implements the EventHandler interface for handling ActionEvents.
 *
 * @see ControllerAction
 * @see EventHandler
 */
public class QuoridorControllerAction extends ControllerAction implements EventHandler<ActionEvent> {

    private QuoridorView qView;

    public QuoridorControllerAction(Model model, View view, Controller control) {
        super(model, view, control);
        qView = (QuoridorView) view;

        setMenuHandlers();
    }

    private void setMenuHandlers() {
        qView.getMenuStart().setOnAction(e -> {
            try {
                control.startGame();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        });

        qView.getMenuQuit().setOnAction(e -> {
            System.exit(0);
        });
    }

    /**
     * Handles the ActionEvent.
     * @param event the ActionEvent to handle
     */
    public void handle(ActionEvent event) {
        if (!model.isCaptureActionEvent()) return;
    }

}
