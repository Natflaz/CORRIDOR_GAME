package quoridor.quoridor_menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import quoridor.control.QuoridorController;

/**
 * ControllerMenu is the controller of the main menu.
 */
public class ControllerMenu implements EventHandler<ActionEvent>  {

    Menu menu;
    ViewMenu app;


    public ControllerMenu(Menu menu, ViewMenu app) {
        this.menu = menu;
        this.app = app;
    }

    /**
     * This method is called when a button is pressed in the menu.
     * @param actionEvent the event that is triggered when a button is pressed.
     */
    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == app.getStart()) {
            app.getRoot().getChildren().clear();
            app.newGamePressed();
            app.addListeners();
            return;
        }
        if (actionEvent.getSource() == app.getQuit()) {
            System.exit(0);
            return;
        }
        if (actionEvent.getSource() == app.getHelp()) {
            app.getRoot().getChildren().clear();
            app.helpIsPressed();
            app.addListeners();
            return;
        }
        if (actionEvent.getSource() == app.getBack()){
            app.getRoot().getChildren().clear();
            app.createMenu();
            app.addListeners();
            return;
        }
        if (actionEvent.getSource() == app.getPvp()) {
            menu.getStage().close();
            menu.getQuoridor().startGame(QuoridorController.GRAPHIC, QuoridorController.GRAPHIC);
            return;
        }
        if (actionEvent.getSource() == app.getPve()) {
            menu.getStage().close();
            menu.getQuoridor().startGame(QuoridorController.MINIMAX, QuoridorController.GRAPHIC);
            return;
        }
        if (actionEvent.getSource() == app.getPvh()) {
            menu.getStage().close();
            menu.getQuoridor().startGame(QuoridorController.COINFLIPPINGAI, QuoridorController.GRAPHIC);
            return;
        }
        if (actionEvent.getSource() == app.getIAvsIA()) {
            menu.getStage().close();
            menu.getQuoridor().startGame(QuoridorController.COINFLIPPINGAI, QuoridorController.COINFLIPPINGAI);
        }
    }
}