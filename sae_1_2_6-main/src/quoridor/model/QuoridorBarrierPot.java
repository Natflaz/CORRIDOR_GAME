package quoridor.model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.GridElement;

/**
 * A barrier pot used in the Quoridor game
 * @see GridElement
 */
public class QuoridorBarrierPot extends GridElement {
    public QuoridorBarrierPot(int x, int y, GameStageModel gameStageModel) {
        super("barrierpot", x, y, 10, 1, gameStageModel);
    }

    /**
     * @return whether the pot is full or not
     */
    public boolean isPotFull() {
        return numberOfBarriers() == nbRows;
    }

    /**
     * convenience method to be used in place of {@link #putElement(GameElement, int, int)}
     * in the context of the quoridorPot. Also sets the barrier type.
     * @param barrier
     */
    public void putBarrier(Barrier barrier) {
        barrier.setBarrierType(BarrierType.POT);
        int row = getRowToPlace();

        putElement(barrier, row, 0);
    }
    public Barrier getBarrier() {
        Barrier barrier = null;
        int i = 0;
        while(barrier == null && i < nbRows)
        {
            GameElement element = getElement(i,0);
            if (element instanceof Barrier)
                barrier =(Barrier) element;
            i++;
        }
        return barrier;
    }

    /**
     * @return the number of barriers
     */
    public int numberOfBarriers()
    {
        int compt = 0;
        for (int i = 0; i < nbRows; i++) {
            if (getElement(i, 0) instanceof Barrier) {
                compt++;
            }
        }
        return compt;
    }
    private int getRowToPlace() {
        for (int i = 0; i < nbRows; i++) {
            if (!(getElement(i, 0) instanceof Barrier)) {
                return i;
            }
        }
        throw new RuntimeException("this barrier pot is full");
    }

}
