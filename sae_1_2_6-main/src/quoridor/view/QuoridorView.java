package quoridor.view;

import boardifier.model.Model;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * This class is used to display the view of the Quoridor game.
 */
public class QuoridorView extends View {

    private MenuItem menuStart;
    private MenuItem menuQuit;

    public QuoridorView(Model model, Stage stage, RootPane rootPane) {
        super(model, stage, rootPane);
    }

    @Override
    protected void createMenuBar() {
        menuBar = new MenuBar();
        Menu menu1 = new Menu("Game");
        menuStart = new MenuItem("New Game");
        menuQuit = new MenuItem("Quit");
        menu1.getItems().addAll(menuStart, menuQuit);
        menuBar.getMenus().add(menu1);
    }

    public MenuItem getMenuStart() {
        return menuStart;
    }

    public MenuItem getMenuQuit() {
        return menuQuit;
    }
}
