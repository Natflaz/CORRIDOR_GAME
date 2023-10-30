package quoridor.control;

import boardifier.model.GameElement;

/**
 * The GameElementTransform class represents a transformed version of a game element.
 * It extends the GameElement class and adds additional properties for row and column coordinates.
 *
 * @see GameElement
 */

public class GameElementTransform extends GameElement {

    private int row;
    private int col;

    public GameElementTransform(GameElement element) {
        super(element.getX(), element.getY(), element.getGameStage(), element.getType());
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
