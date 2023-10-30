package quoridor.view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import quoridor.model.Barrier;
import quoridor.model.BarrierType;

/**
 * Class BarrierLook which is the view of the barrier.
 * @see ElementLook
 * @see Barrier
 */
public class BarrierLook extends ElementLook {

    private Rectangle rect;
    private boolean fistTime = true;

    public BarrierLook(GameElement element) {
        super(element, 50);
        onChange();
    }

    /**
     * Method onChange which is called when the barrier is changed.
     */
    @Override
    public void onChange() { // TODO: look of the barrier
        Barrier barrier = (Barrier) element;

        BarrierType type = barrier.getBarrierType();
        if (rect == null) {
            rect = new Rectangle();
        }
        if(type == BarrierType.POT) {
            if (barrier.getColor() == Barrier.BARRIER_BLACK){
                rect.setWidth(25);
                rect.setHeight(10);
                rect.setFill(Color.RED);
            } else {
                rect.setWidth(25);
                rect.setHeight(10);
                rect.setFill(Color.BLUE);
            }
            rect.setX(-12);
            rect.setY(-5);
            // an image of a barrier (some stones ?)
        } else if(type==BarrierType.VERTICAL) {
            if (barrier.getColor() == Barrier.BARRIER_BLACK){
                rect.setWidth(10);
                rect.setHeight(125);
                rect.setOpacity(1);
                rect.setFill(Color.RED);
                rect.setX(33);
                rect.setY(-25);
            } else {
                rect.setWidth(10);
                rect.setHeight(125);
                rect.setOpacity(1);
                rect.setFill(Color.BLUE);
                rect.setX(33);
                rect.setY(-25);
            }
            // an image of a vertical wall (medieval)
        } else if (type == BarrierType.HORIZONTAL) {
            if (barrier.getColor() == Barrier.BARRIER_BLACK){
                rect.setWidth(125);
                rect.setHeight(10);
                rect.setOpacity(1);
                rect.setFill(Color.RED);
                rect.setX(-25);
                rect.setY(33);
            } else {
                rect.setWidth(125);
                rect.setHeight(10);
                rect.setOpacity(1);
                rect.setFill(Color.BLUE);
                rect.setX(-25);
                rect.setY(33);
            }
            // an image of a horizontal wall (medieval)
        } else if (type == BarrierType.PREVIEW_VERTICAL) {
            if (barrier.getColor() == Barrier.BARRIER_BLACK){
                rect.setWidth(10);
                rect.setHeight(125);
                rect.setOpacity(0.7);
                rect.setFill(Color.RED);
                rect.setX(33);
                rect.setY(-25);
            } else {
                rect.setWidth(10);
                rect.setHeight(125);
                rect.setOpacity(0.7);
                rect.setFill(Color.BLUE);
                rect.setX(33);
                rect.setY(-25);
            }
            // an image of a horizontal wall (medieval)
        } else if (type == BarrierType.PREVIEW_HORIZONTAL) {
            if (barrier.getColor() == Barrier.BARRIER_BLACK){
                rect.setWidth(125);
                rect.setHeight(10);
                rect.setOpacity(0.7);
                rect.setFill(Color.RED);
                rect.setX(-25);
                rect.setY(33);
            } else {
                rect.setWidth(125);
                rect.setHeight(10);
                rect.setOpacity(0.7);
                rect.setFill(Color.BLUE);
                rect.setX(-25);
                rect.setY(33);
            }
            // an image of a horizontal wall (medieval)
        }
        if (fistTime) {
            addShape(rect);
            fistTime = false;
        }
    }
}


