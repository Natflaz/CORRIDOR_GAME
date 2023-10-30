package quoridor.control;

import quoridor.model.Barrier;
import quoridor.model.Pawn;
import quoridor.model.QuoridorBoard;
import quoridor.model.QuoridorStageModel;

import java.util.HashMap;
import java.util.Map;

/**
 * The PiecesRecorder class represents a utility class for recording and managing the state of pieces in a game.
 * It provides methods to record and track the positions and status of game pieces.
 *
 */
public class PiecesRecorder {

    private final QuoridorBoard quoridorBoard;
    private final Map<Pawn,GameElementTransform> pawnRecords = new HashMap<Pawn, GameElementTransform>();
    private final Map<Barrier, BarrierTransform> barrierRecords = new HashMap<Barrier, BarrierTransform>();

    /**
     * Creates an instance so that moves can be made and reverted at will
     * @param stageModel for which we will record the pieces
     */
    public PiecesRecorder(QuoridorStageModel stageModel) {
        createRecord(stageModel.getWhitePawn());
        createRecord(stageModel.getBlackPawn());

        quoridorBoard = stageModel.getBoard();

        for (Barrier barrier : stageModel.getBlackBarrierArray()) {
            createRecord(barrier);
        }
        for (Barrier barrier : stageModel.getWhiteBarrierArray()) {
            createRecord(barrier);
        }
    }

    /**
     * takes a snapshot of all the moving elements in the QuoridorStageModel, to be restored later via {@link #revertToSnapshot()}
     */
    public void takeSnapshot() {

        for (Map.Entry<Pawn, GameElementTransform> entry :  pawnRecords.entrySet()) {
            Pawn pawn = entry.getKey();
            GameElementTransform recordedPosition = entry.getValue();

            takePawnSnapshot(pawn, recordedPosition);
        }

        for (Map.Entry<Barrier, BarrierTransform> entry :  barrierRecords.entrySet()) {
            Barrier barrier = entry.getKey();
            BarrierTransform barrierTransform = entry.getValue();
            takeBarrierSnapshot(barrier, barrierTransform);
        }
    }

    /**
     * reverts the position of every GameElement on the board to the state in which it was when {@link #takeSnapshot()} was last called
     */
    public void revertToSnapshot() {
        for (Map.Entry<Pawn, GameElementTransform> entry : pawnRecords.entrySet()) {
            Pawn pawn = entry.getKey();
            GameElementTransform recordedPosition = entry.getValue();

            if (pawn.getGrid() != recordedPosition.getGrid()) {
                pawn.getGrid().removeElement(pawn);
                recordedPosition.getGrid().putElement(pawn, recordedPosition.getRow(), recordedPosition.getCol());
            } else {
                quoridorBoard.moveElement(pawn, recordedPosition.getRow(), recordedPosition.getCol());
            }
        }

        for (Map.Entry<Barrier, BarrierTransform> entry : barrierRecords.entrySet()) {
            Barrier barrier = entry.getKey();
            BarrierTransform barrierTransform = entry.getValue();

            if (barrier.getGrid() != barrierTransform.getGrid()) {
                barrier.getGrid().removeElement(barrier);
                barrier.setBarrierType(barrierTransform.getBarrierType());
                barrierTransform.getGrid().putElement(barrier, barrierTransform.getRow(), barrierTransform.getCol());
            } else {
                barrier.getGrid().moveElement(barrier, barrierTransform.getRow(), barrierTransform.getCol());
            }
        }
    }

    /**
     * @param barrier is an element for which we will create a record when the instance is created
     */
    private void createRecord(Barrier barrier) {
        barrierRecords.put(barrier, new BarrierTransform(barrier, barrier.getBarrierType()));
    }

    private void createRecord(Pawn pawn) {
        pawnRecords.put(pawn, new GameElementTransform(pawn));
    }

    /**
     * takes a snapshot of the position and type of the barrier
     * @param barrier
     * @param barrierTransform
     */
    private void takeBarrierSnapshot(Barrier barrier, BarrierTransform barrierTransform) {
        int[] currentCellPosition =  barrier.getGrid().getElementCell(barrier);

        // TODO : modifiy barrier/pawn transform to keep a reference to the grid
        barrierTransform.setRow(currentCellPosition[0]);
        barrierTransform.setCol(currentCellPosition[1]);
        barrierTransform.setBarrierType(barrier.getBarrierType());
        barrierTransform.setGrid(barrier.getGrid());
    }

    /**
     * takes a snapshot of the position of the pawn
     * @param pawn
     * @param recordedPosition
     */
    private void takePawnSnapshot(Pawn pawn, GameElementTransform recordedPosition) {
        int[] currentCellPosition =  quoridorBoard.getElementCell(pawn);

        recordedPosition.setRow(currentCellPosition[0]);
        recordedPosition.setCol(currentCellPosition[1]);
        recordedPosition.setGrid(pawn.getGrid());
    }




}