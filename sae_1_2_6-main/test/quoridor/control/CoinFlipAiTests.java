package quoridor.control;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CoinFlipAiTests {


    @Mock
    private Model model;
    @Mock
    private GameStageModel gameStageModel;
    @Mock
    private CoinFlipper flipper;
    @Mock
    private QuoridorController control;

    @Mock
    private QuoridorBoard board;

    @Mock
    private Barrier barrier;
    @Mock
    private QuoridorStageModel quoridorStageModel;
    @Mock
    private Graph graph;
    @Mock
    private Pawn whitePawn;
    @Mock
    private Pawn blackPawn;
    @Mock
    private ActionList actionList;
    @Mock
    private GameAction gameAction;
    @Mock
    private BarrierMoveAction barrierMoveAction;
    @Mock
    private Node node;
    @BeforeEach
    public void init(){
        this.control = mock(QuoridorController.class);
        this.model = mock(Model.class);
        this.flipper = mock(CoinFlipper.class);
        this.board = mock(QuoridorBoard.class);
        this.barrier = mock(Barrier.class);
        this.graph = mock(Graph.class);
        this.quoridorStageModel = mock(QuoridorStageModel.class);
        this.node = mock(Node.class);
        this.whitePawn = mock(Pawn.class);
        this.blackPawn = mock(Pawn.class);;
        this.actionList = mock(ActionList.class);
        this.gameAction = mock(GameAction.class);
        this.barrierMoveAction = mock(BarrierMoveAction.class);


    }


    @Test
    public void testPlaceWallDecide() {
        when(flipper.flipCoin()).thenReturn(true);
        when(model.getIdPlayer()).thenReturn(1);
        when(control.testIfThereIsStillAPathAfterAction(any(BarrierMoveAction.class))).thenReturn(true);
        when(control.testIfThereIsStillAPathAfterAction((BarrierMoveAction) any(GameAction.class))).thenReturn(true);
        when(model.getGameStage()).thenReturn(quoridorStageModel);
        when(quoridorStageModel.getBlackBarrierToPlay()).thenReturn(barrier);
        when(quoridorStageModel.getBoard()).thenReturn(board);
        when(board.canPlaceBarrier(1, 1, BarrierType.HORIZONTAL)).thenReturn(true);
        when(graph.bestPathToWinOpponent(quoridorStageModel, model)).thenReturn(List.of(node, node, node, node, node, node, node, node));
        when(graph.bestPathToWin(1)).thenReturn(List.of(node, node, node, node, node, node, node, node));
        when(quoridorStageModel.isPotEmpty()).thenReturn(false);
        when(quoridorStageModel.getBlackPawn()).thenReturn(blackPawn);
        when(quoridorStageModel.getWhitePawn()).thenReturn(whitePawn);
        when(board.getElementCell(whitePawn)).thenReturn(new int[]{0, 8});
        when(board.getElementCell(blackPawn)).thenReturn(new int[]{16, 8});


        CoinFlipDecider ai = new CoinFlipDecider(model, control, flipper, graph);
        ActionList actions = ai.decide();


        verify(model, times(1)).getIdPlayer();
        verify(model, times(4)).getGameStage();
        verify(quoridorStageModel, times(3)).getBoard();
        verify(graph, times(2)).bestPathToWin(anyInt());
        verify(quoridorStageModel, times(1)).isPotEmpty();
        verify(quoridorStageModel, times(3)).getBlackPawn();
        verify(quoridorStageModel, times(2)).getWhitePawn();
        verify(board, times(4)).getElementCell(any(Pawn.class));

    }

    @Test
    public void testPawnMoveDecide() {
        when(flipper.flipCoin()).thenReturn(false);
        when(model.getIdPlayer()).thenReturn(0);
        when(control.testIfThereIsStillAPathAfterAction(any(BarrierMoveAction.class))).thenReturn(true);
        when(control.testIfThereIsStillAPathAfterAction((BarrierMoveAction) any(GameAction.class))).thenReturn(true);
        when(model.getGameStage()).thenReturn(quoridorStageModel);
        when(quoridorStageModel.getBlackBarrierToPlay()).thenReturn(barrier);
        when(quoridorStageModel.getBoard()).thenReturn(board);
        when(board.canPlaceBarrier(1, 1, BarrierType.HORIZONTAL)).thenReturn(true);
        when(graph.bestPathToWinOpponent(quoridorStageModel, model)).thenReturn(List.of(node, node, node, node, node, node, node, node));
        when(graph.bestPathToWin(0)).thenReturn(List.of(node, node, node, node, node, node, node, node));
        when(quoridorStageModel.isPotEmpty()).thenReturn(false);
        when(quoridorStageModel.getBlackPawn()).thenReturn(blackPawn);
        when(quoridorStageModel.getWhitePawn()).thenReturn(whitePawn);
        when(board.getElementCell(whitePawn)).thenReturn(new int[]{0, 8});
        when(board.getElementCell(blackPawn)).thenReturn(new int[]{16, 8});


        CoinFlipDecider ai = new CoinFlipDecider(model, control, flipper, graph);
        ActionList actions = ai.decide();
        ActionList actionList1 = new ActionList(true);
        actionList.addSingleAction(gameAction);


        verify(model, times(1)).getIdPlayer();
        verify(model, times(4)).getGameStage();
        verify(quoridorStageModel, times(2)).getBoard();
        verify(graph, times(1)).bestPathToWin(anyInt());
        verify(quoridorStageModel, times(2)).getBlackPawn();
        verify(quoridorStageModel, times(1)).getWhitePawn();
        verify(board, times(2)).getElementCell(any(Pawn.class));
    }


}
