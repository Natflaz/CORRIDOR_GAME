package quoridor.control;

import boardifier.model.GameElement;
import quoridor.model.BarrierType;

/**
 * The BarrierTransform class represents a transformed version of a barrier game element.
 * It extends the GameElementTransform class and adds additional properties for the type of barrier.
 *
 * @see GameElementTransform
 */
public class BarrierTransform extends GameElementTransform {
    private BarrierType barrierType;

    public BarrierTransform(GameElement element, BarrierType type) {
        super(element);
        this.barrierType = type;
    }

    public BarrierType getBarrierType() {
        return barrierType;
    }

    public void setBarrierType(BarrierType barrierType) {
        this.barrierType = barrierType;
    }
}
