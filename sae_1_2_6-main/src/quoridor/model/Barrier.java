package quoridor.model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

/**
 * Barrier class represents a barrier in the game.
 * @see GameElement
 */
public class Barrier extends GameElement {
    private final int color;
    public static int BARRIER_BLACK = 0;
    public static int BARRIER_WHITE = 1;

    private BarrierType barrierType = BarrierType.POT;

    public Barrier(int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        ElementTypes.register("barrier", 50);
        type = ElementTypes.getType("barrier");
        this.color = color;
        setLocationType(LOCATION_TOPLEFT); // marche pas pour ce que je veux, mais aurait pu.
    }

    public int getColor() {
        return color;
    }

    public BarrierType getBarrierType()
    {
        return barrierType;
    }
    public void setBarrierType( BarrierType barrierType)
    {
        this.barrierType =barrierType;
        lookChanged = true;
    }
}

