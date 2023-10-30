package quoridor.control;

import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import quoridor.model.BarrierType;
import quoridor.model.Direction;
import quoridor.model.QuoridorBoard;
import quoridor.model.QuoridorStageModel;
import quoridor.model.action.BarrierMoveAction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class MovesVerifierTests {
    @Mock
    private Model model;

    @Mock
    private QuoridorController controller;
    @Mock
    private  GameElement gameElement;

    private MovesVerifier movesVerifier;

    @BeforeEach
    public void setUp() {
        controller = mock(QuoridorController.class);
        model = mock(Model.class);
        gameElement = mock(GameElement.class);
        movesVerifier = new MovesVerifier(model, controller);
    }
//TODO : Finish that
    @Test
    public void testVerify_WithValidBarrierMoveAction_ReturnsTrue() {
        QuoridorBoard quoridorBoard = mock(QuoridorBoard.class);
        when(quoridorBoard.canPlaceBarrier(anyInt(), anyInt(), any())).thenReturn(true);
        when(quoridorBoard.noWallOrEdgeInDirection(anyInt(), anyInt(), any())).thenReturn(true);

        QuoridorStageModel quoridorStageModel = mock(QuoridorStageModel.class);
        when(quoridorStageModel.getBoard()).thenReturn(quoridorBoard);

        when(model.getGameStage()).thenReturn(quoridorStageModel);
        when(model.getIdPlayer()).thenReturn(QuoridorStageModel.BLACK);
        when(gameElement.getX()).thenReturn(0.0);
        when(gameElement.getY()).thenReturn(0.0);
        BarrierMoveAction barrierMoveAction = mock(BarrierMoveAction.class);
        when(barrierMoveAction.getRowDest()).thenReturn(1);
        when(barrierMoveAction.getColDest()).thenReturn(1);
        when(barrierMoveAction.getBarrierType()).thenReturn(BarrierType.HORIZONTAL);

        ActionList actionList = mock(ActionList.class);
        BarrierMoveAction gameAction = mock(BarrierMoveAction.class);
        when(actionList.getActions()).thenReturn(List.of(List.of(gameAction)));


        boolean result = movesVerifier.verify(actionList);

        assertTrue(result);
        verify(quoridorBoard).canPlaceBarrier(1, 1, BarrierType.HORIZONTAL);
        verify(quoridorBoard).noWallOrEdgeInDirection(1, 1, Direction.NORTH);
        verify(quoridorBoard).noWallOrEdgeInDirection(1, 1, Direction.SOUTH);
    }
}
