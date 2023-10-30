package quoridor.view;

import boardifier.view.RootPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class is used to display the root pane of the Quoridor game.
 * @see RootPane
 */
public class QuoridorRootPane extends RootPane {
    public QuoridorRootPane() {
        super();
    }

    /**
     * Method createDefaultGroup which is called when the root pane is created.
     */
    @Override
    public void createDefaultGroup() {
        Rectangle frame = new Rectangle(1000, 750, Color.WHITE);
        Text text = new Text("Playing to The Quoridor");
        text.setFont(new Font(15));
        text.setFill(Color.BLACK);
        text.setX(10);
        text.setY(20);
        // put shapes in the group
        group.getChildren().clear();
        group.getChildren().addAll(frame, text);
    }
}
