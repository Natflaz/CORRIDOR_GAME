package quoridor.model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * A pawn used in the Quoridor game
 * @see GameElement
 */
public class Pawn extends GameElement {

    private int color;
    public static int PAWN_BLACK = 0;
    public static int PAWN_WHITE = 1;

    public Pawn(int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("pawn",55);
        type = ElementTypes.getType("pawn");
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
