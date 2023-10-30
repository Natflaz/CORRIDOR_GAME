package quoridor.model.action;

import boardifier.model.Model;
import boardifier.model.action.MoveAction;
import quoridor.model.Barrier;
import quoridor.model.BarrierType;

/**
 * BarrierMoveAction class represents a move action for a barrier.
 * @see MoveAction
 */
public class BarrierMoveAction extends MoveAction {
    protected BarrierType barrierType;
    public BarrierMoveAction(Model model, Barrier barrier, String gridDest, int rowDest, int colDest, BarrierType barrierType, String animationName, double xDest, double yDest, double factor) {
        super(model, barrier, gridDest, rowDest, colDest, animationName, xDest, yDest, factor);
        this.barrierType = barrierType;
    }

    public BarrierMoveAction(Model model, Barrier barrier, String gridDest, int rowDest, int colDest, BarrierType barrierType) {
        super(model, barrier, gridDest, rowDest, colDest);
        this.barrierType = barrierType;
    }

    /**
     * Executes the action.
     */
    @Override
    public void execute() {
        ((Barrier)element).setBarrierType(barrierType);
        super.execute();
    }

    public BarrierType getBarrierType() {
        return barrierType;
    }
}
