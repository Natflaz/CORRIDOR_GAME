package quoridor.control;

import org.junit.jupiter.api.Test;
import quoridor.model.Barrier;
import quoridor.model.Pawn;
import quoridor.model.QuoridorBoard;
import quoridor.model.QuoridorStageModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PiecesRecorderTests {

    @Test
    public void recorderTest() {
        QuoridorStageModel stageModel = mock(QuoridorStageModel.class);
        when(stageModel.getWhiteBarrierArray()).thenReturn(new Barrier[10]);
        when(stageModel.getBlackBarrierArray()).thenReturn(new Barrier[10]);
        when(stageModel.getWhitePawn()).thenReturn(mock(Pawn.class));
        when(stageModel.getBlackPawn()).thenReturn(mock(Pawn.class));
        QuoridorBoard board = mock(QuoridorBoard.class);

        Barrier[] barriers = new Barrier[2];
        barriers[0] = mock(Barrier.class);
        barriers[1] = mock(Barrier.class);
        when(barriers[0].getGrid()).thenReturn(board);
        when(barriers[1].getGrid()).thenReturn(board);
        when(stageModel.getBlackBarrierArray()).thenReturn(barriers);
        when(stageModel.getWhiteBarrierArray()).thenReturn(barriers);

        when(board.getElementCell(any(Pawn.class))).thenReturn(new int[]{0,0}, new int[]{8,8});
        when(board.getElementCell(any(Barrier.class))).thenReturn(new int[]{2, 3});
        when(stageModel.getBoard()).thenReturn(board);
        PiecesRecorder piecesRecorder = new PiecesRecorder(stageModel);

        piecesRecorder.takeSnapshot();

        board = mock(QuoridorBoard.class);
        when(barriers[0].getGrid()).thenReturn(board);
        when(barriers[1].getGrid()).thenReturn(board);
        piecesRecorder.revertToSnapshot();

    }
}
