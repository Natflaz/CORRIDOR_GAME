package quoridor.model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;

/**
 * A factory for the Quoridor game
 * @see StageElementsFactory
 */
public class QuoridorStageFactory extends StageElementsFactory {

    private QuoridorStageModel stageModel;

    public QuoridorStageFactory(GameStageModel model) {
        super(model);
        stageModel = (QuoridorStageModel) model;
    }

    /**
     * Creates the elements of the game
     */
    @Override
    public void setup() {
        // create the board
        stageModel.setBoard(new QuoridorBoard(20,40, stageModel));
        // create the pots
        QuoridorBarrierPot blackBarrierPot = new QuoridorBarrierPot(800, 40, model);
        stageModel.setBlackBarrierPot(blackBarrierPot);
        QuoridorBarrierPot whiteBarrierPot = new QuoridorBarrierPot(900, 40, model);
        stageModel.setWhiteBarrierPot(whiteBarrierPot);

        // create tge pawns & barrier
        Barrier[] blackBarrier = new Barrier[10];
        for (int i = 0 ; i < 10 ; i++) {
            blackBarrier[i] = new Barrier(Barrier.BARRIER_BLACK, model);
        }
        stageModel.setBlackBarrierArray(blackBarrier);
        Barrier[] whiteBarrier = new Barrier[10];
        for (int i = 0 ; i < 10 ; i++) {
            whiteBarrier[i] = new Barrier(Barrier.BARRIER_WHITE, model);
        }
        stageModel.setWhiteBarrierArray(whiteBarrier);
        Pawn black = new Pawn(Pawn.PAWN_BLACK, model);
        stageModel.setBlackPawn(black);
        Pawn white = new Pawn(Pawn.PAWN_WHITE, model);
        stageModel.setWhitePawn(white);

        // assign pawns & barrier to their pot : they will be put at the center
        for (int i = 0 ; i < 10 ; i++) {
            blackBarrierPot.putBarrier(blackBarrier[i]);
            whiteBarrierPot.putBarrier(whiteBarrier[i]);
        }
        stageModel.getBoard().initializePawnPosition(white,Direction.NORTH);
        stageModel.getBoard().initializePawnPosition(black,Direction.SOUTH);

//        stageModel.getBoard().putElement(black, 0, 8);
//        stageModel.getBoard().putElement(white, 16, 8);
        TextElement text = new TextElement(stageModel.getCurrentPlayerName(), stageModel);
        text.setLocation(10,30);
        text.setLocationType(GameElement.LOCATION_TOPLEFT);
        stageModel.setPlayerName(text);
    }
}
