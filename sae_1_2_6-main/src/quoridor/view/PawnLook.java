package quoridor.view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import quoridor.model.Pawn;

/**
 * PawnLook class
 * PawnLook is the class that represents the look of a pawn
 * @see ElementLook
 * @see Pawn
 */
public class PawnLook extends ElementLook {

    Rectangle p;

    public PawnLook(GameElement element) {
        super(element, 10);
        Pawn pawn = (Pawn) element;

        if (pawn.getColor() == Pawn.PAWN_BLACK) {
            //addShape(p = new Rectangle(50, 50, Color.BLACK));
            addShape(p = new Rectangle(50, 50, new ImagePattern(new Image("file:assets/red_idle.png"), 0, 0, 1, 1, true)));

        }
        else {
            //addShape(p = new Rectangle(50, 50, Color.WHEAT));
            addShape(p = new Rectangle(50, 50, new ImagePattern(new Image("file:assets/blue_idle.png"), 0, 0, 1, 1, true)));

        }
    }

    /**
     * Method onSelectionChange which is called when the pawn is selected.
     */
    @Override
    public void onSelectionChange() {
        Pawn pawn = (Pawn)getElement();
        if (pawn.isSelected()) {
            p.setStrokeWidth(3);
            p.setStrokeMiterLimit(10);
            p.setStrokeType(StrokeType.CENTERED);
            p.setStroke(Color.valueOf("0x333333"));
        }
        else {
            p.setStrokeWidth(0);
        }
    }

    /**
     * Method onChange which is called when the pawn is changed.
     */
    @Override
    public void onChange() {

    }
}
