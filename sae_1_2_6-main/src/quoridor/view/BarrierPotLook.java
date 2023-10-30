package quoridor.view;

import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * Class BarrierPotLook which is the view of the barrier pot.
 * @see GridLook
 */
public class BarrierPotLook extends GridLook {

    private Rectangle[] cells;

    public BarrierPotLook(int cellWidth, int cellHeight, GameElement element, Model model, String playerName) { // TODO: see if cellWidth, cellHeight, borderWidth, borderColor is good
        super(cellWidth, cellHeight, cellWidth, cellHeight, 1, "#000000", element);
        cells = new Rectangle[10];
        int j = 0;
        for (int i = 0 ; i < 10 ; i++) {
            cells[i] = new Rectangle(cellWidth, cellHeight, Color.WHITE);
            cells[i].setStrokeWidth(3);
            cells[i].setStrokeMiterLimit(10);
            cells[i].setStrokeType(StrokeType.CENTERED);
            cells[i].setStroke(Color.valueOf("0x333333"));
            cells[i].setX(borderWidth);
            cells[i].setY(i*this.cellHeight+borderWidth);
            addShape(cells[i]);
            j = i+1;
        }
    }

    /**
     * Method onChange which is called when the barrier pot is changed.
     */
    @Override
    public void onChange() {

    }
}
