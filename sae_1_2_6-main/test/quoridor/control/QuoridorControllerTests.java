package quoridor.control;


import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import quoridor.model.QuoridorBoard;
import quoridor.model.QuoridorStageModel;
import quoridor.model.*;

import java.io.BufferedReader;

import static org.mockito.Mockito.*;

public class QuoridorControllerTests {
    @Mock
    private static Player black,white;
    @Mock
    private static Model model;
    @Mock
    private static QuoridorStageModel stageModel;
    @Mock
    private static View view;
    @Mock
    private static QuoridorBoard board;
    @Mock
    private static QuoridorBarrierPot blackPot, whitePot;
    @Mock
    private static  Barrier bb1,bb2,bb3,bb4,bb5,bb6,bb7,bb8,bb9,bb10;
    @Mock
    private static  Barrier bw1,bw2,bw3,bw4,bw5,bw6,bw7,bw8,bw9,bw10;
    @Mock
    private static MinimaxDecider minimaxDecider;
    @Mock
    private static CoinFlipDecider coinFlipDecider;
    private static Graph graph;

    @BeforeAll
    public void setup()
    {
        model = mock(Model.class);
        view = mock(View.class);

        black = mock(Player.class);
        white = mock(Player.class);

        board = mock(QuoridorBoard.class);
        blackPot = mock(QuoridorBarrierPot.class);
        whitePot = mock(QuoridorBarrierPot.class);
        stageModel = mock(QuoridorStageModel.class);
        graph = mock(Graph.class);
        minimaxDecider = mock(MinimaxDecider.class);
        coinFlipDecider = mock(CoinFlipDecider.class);

        bw1 = mock(Barrier.class);
        bw2 = mock(Barrier.class);
        bw3 = mock(Barrier.class);
        bw4 = mock(Barrier.class);
        bw5 = mock(Barrier.class);
        bw6 = mock(Barrier.class);
        bw7 = mock(Barrier.class);
        bw8 = mock(Barrier.class);
        bw9 = mock(Barrier.class);
        bw10 = mock(Barrier.class);

        bb1 = mock(Barrier.class);
        bb2 = mock(Barrier.class);
        bb3 = mock(Barrier.class);
        bb4 = mock(Barrier.class);
        bb5 = mock(Barrier.class);
        bb6 = mock(Barrier.class);
        bb7 = mock(Barrier.class);
        bb8 = mock(Barrier.class);
        bb9 = mock(Barrier.class);
        bb10 = mock(Barrier.class);

        baseBarrierWhenWhite(bw1);
        baseBarrierWhenWhite(bw2);
        baseBarrierWhenWhite(bw3);
        baseBarrierWhenWhite(bw4);
        baseBarrierWhenWhite(bw5);
        baseBarrierWhenWhite(bw6);
        baseBarrierWhenWhite(bw7);
        baseBarrierWhenWhite(bw8);
        baseBarrierWhenWhite(bw9);
        baseBarrierWhenWhite(bw10);

        baseBarrierWhenBlack(bb1);
        baseBarrierWhenBlack(bb2);
        baseBarrierWhenBlack(bb3);
        baseBarrierWhenBlack(bb4);
        baseBarrierWhenBlack(bb5);
        baseBarrierWhenBlack(bb6);
        baseBarrierWhenBlack(bb7);
        baseBarrierWhenBlack(bb8);
        baseBarrierWhenBlack(bb9);
        baseBarrierWhenBlack(bb10);

        when(black.getType()).thenReturn(Player.HUMAN);
        when(white.getType()).thenReturn(Player.HUMAN);

        when(model.getGameStage()).thenReturn(stageModel);
        when(stageModel.getBoard()).thenReturn(board);

        when(stageModel.getBlackBarrierPot()).thenReturn(blackPot);
        when(stageModel.getWhiteBarrierPot()).thenReturn(whitePot);

        Barrier[] blackBarriers = new Barrier[]{bb1,bb2,bb3,bb4,bb5,bb6,bb7,bb8,bb9,bb10};
        Barrier[] whiteBarriers = new Barrier[]{bw1,bw2,bw3,bw4,bw5,bw6,bw7,bw8,bw9,bw10};
        when(stageModel.getBlackBarrierArray()).thenReturn(blackBarriers);
        when(stageModel.getWhiteBarrierArray()).thenReturn(whiteBarriers);
    }

    private static void baseBarrierWhenBlack(Barrier barrier) {
        when(barrier.getColor()).thenReturn(Barrier.BARRIER_BLACK);
        when(barrier.getBarrierType()).thenReturn(BarrierType.POT);
    }
    private static void baseBarrierWhenWhite(Barrier barrier) {
        when(barrier.getColor()).thenReturn(Barrier.BARRIER_WHITE);
        when(barrier.getBarrierType()).thenReturn(BarrierType.POT);
    }

    @Test
    public void playerCreationTest()
    {
        //TODO : finish this test

        BufferedReader bufferedReader = mock(BufferedReader.class);
        StageFactory.registerModelAndView("quoridor", "quoridor.model.QuoridorStageModel", "quoridor.view.QuoridorStageView");
        QuoridorController control = new QuoridorController(model, view, 0, 0);
        control.setFirstStageName("quoridor");


        when(model.getCurrentPlayer()).thenReturn(black);
    }
    @Test
    public void testPlay11Barrier() {

        //init
        //Since the controller counts which barrier gets placed where, we check that one player placing 11 barriers doesn't thtow an exception

        //String gameInputs = "WA8V\nPS\nwA7H\npS\nWa6V\nPS\nWA5h\nPS\nwa4V\nPS\nwA3h\nPs\nPN\nPe\nWa2v\nPS\nwA1h\nPW\nWH1V\nPN\nWH8H\nPn\nWH5H\nps";

        InputLineInterpreter inputLineInterpreter = mock(InputLineInterpreter.class);
        MinimaxDecider minimaxDecider = mock(MinimaxDecider.class);
        CoinFlipDecider coinFlipDecider = mock(CoinFlipDecider.class);
        BufferedReader bufferedReader = mock(BufferedReader.class);

        StageFactory.registerModelAndView("quoridor", "quoridor.model.QuoridorStageModel", "quoridor.view.QuoridorStageView");
        //QuoridorController control = new QuoridorController(model,view, new BufferedReader(new StringReader(gameInputs)),0,0,graph);
        QuoridorController control = new QuoridorController(model,view, 0, 0);
        control.setFirstStageName("quoridor");


        when(model.getCurrentPlayer()).thenReturn(black);

        try {
            control.startGame();
        }
        catch(GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }
}