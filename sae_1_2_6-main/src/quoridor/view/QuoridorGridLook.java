package quoridor.view;

import boardifier.model.GameElement;
import boardifier.view.GridLook;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import quoridor.model.Barrier;
import quoridor.model.QuoridorBoard;

/**
 * This class is used to display the grid of the Quoridor game.
 * @see GridLook
 * @see QuoridorBoard
 */
public class QuoridorGridLook extends GridLook {

    private Rectangle[][] cells;
    private Circle[][] circle;

    public QuoridorGridLook(int size, GameElement element) {
        super(size, size, (size-20)/9, (size-20)/9, 10, "0x000000", element);
        cells = new Rectangle[9][9];
        circle = new Circle[9][9];
        // create the rectangles.
        for (int i=0;i<9;i++) {
            for(int j=0;j<9;j++) {
                Color c;
                if ((i+j)%2 == 0) {
                    c = Color.BEIGE;
                }
                else {
                    c = Color.DARKGRAY;
                }
                cells[i][j] = new Rectangle(cellWidth, cellHeight, c);
                cells[i][j].setX(j*cellWidth+borderWidth);
                cells[i][j].setY(i*cellHeight+borderWidth);
                addShape(cells[i][j]);
                circle[i][j] = new Circle(cells[i][j].getX() + (double) (size - 20) /9, cells[i][j].getY() + (double) (size - 20) /9, 5, Color.valueOf("0x00FF00"));
            }
        }
    }

    /**
     * Method onChange which is called when the board is changed.
     */
    @Override
    public void onChange() {
        QuoridorBoard board = (QuoridorBoard) element;
        GameElement el = null;
        if (board.getModel().getSelected().size() > 0) {
            el = board.getModel().getSelected().get(0);
        }
        boolean barrier = el instanceof Barrier;
        boolean[][] reach = board.getReachableCells();
        for (int i = 0 ; i < board.getNbRows() ; i++){
            for (int j = 0 ; j < board.getNbCols() ; j++) {
                if (reach[i][j]) {
                    if (barrier) {
                        // color the intersection
                        cells[i][j].setStrokeWidth(0);
                        removeShape(circle[i][j]);
                        addShape(circle[i][j]);
                    } else {
                        cells[i][j].setStrokeWidth(3);
                        cells[i][j].setStrokeMiterLimit(5);
                        cells[i][j].setStrokeType(StrokeType.CENTERED);
                        cells[i][j].setStroke(Color.valueOf("0x00FF00"));
                        removeShape(circle[i][j]);
                    }
                } else {
                    cells[i][j].setStrokeWidth(0);
                    removeShape(circle[i][j]);
                }
            }
        }
    }

    private void removeShape(Shape shape) {
        getGroup().getChildren().remove(shape);
        getShapes().remove(shape);
    }
}
