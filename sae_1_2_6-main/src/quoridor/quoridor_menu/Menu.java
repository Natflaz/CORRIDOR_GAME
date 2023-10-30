package quoridor.quoridor_menu;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import quoridor.Quoridor;

import java.awt.*;
import java.net.URI;

/**
 * Classe Menu which is the main class of the game's main menu.
 */
public class Menu extends Application {


    ViewMenu view = new ViewMenu(this);

    private Quoridor quoridor;
    private Stage stage;


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * This method is called when the application is launched.
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        view.setRoot(new StackPane());
        primaryStage.setTitle("Menu");
        view.createMenu();
        //view.newGamePressed();
        view.addListeners();
        primaryStage.setScene(new Scene(view.getRoot(),500,500)); //mettre le conteneur comme contenu de la fenêtre.
        primaryStage.show();
        stage = primaryStage;
    }

    /**
     * Used to redirect the user to the wikipedia's game page.
     */
    public void redirectToWebsite() {
        Task<Void> task = new Task<Void>() {
            protected Void call() {
                try {
                    Desktop.getDesktop().browse(new URI("https://en.wikipedia.org/wiki/Quoridor"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    public void setQuoridor(Quoridor quoridor) {
        this.quoridor = quoridor;
    }

    public Quoridor getQuoridor() {
        return quoridor;
    }

    public Stage getStage() {
        return stage;
    }
}