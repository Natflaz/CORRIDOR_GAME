package quoridor.model;

import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class QuoridorStageFactoryTests {

    @Test
    public void testSetup() {
        QuoridorStageModel quoridorStageModel = new QuoridorStageModel("quoridor", mock(Model.class));
        QuoridorStageFactory quoridorStageFactory = new QuoridorStageFactory(quoridorStageModel);

        quoridorStageFactory.setup();

        Assertions.assertEquals(10, quoridorStageModel.getBlackBarrierPot().numberOfBarriers());
        Assertions.assertEquals(10, quoridorStageModel.getWhiteBarrierPot().numberOfBarriers());
        Assertions.assertEquals(10, quoridorStageModel.getBlackBarrierArray().length);
        Assertions.assertEquals(10, quoridorStageModel.getWhiteBarrierArray().length);
        Assertions.assertEquals(Pawn.PAWN_BLACK, quoridorStageModel.getBlackPawn().getColor());
        Assertions.assertEquals(Pawn.PAWN_WHITE, quoridorStageModel.getWhitePawn().getColor());
        int[] black = new int[]{16, 8};
        int[] white = new int[]{0, 8};
        Assertions.assertEquals(black[0], quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getBlackPawn())[0]);
        Assertions.assertEquals(black[1], quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getBlackPawn())[1]);
        Assertions.assertEquals(white[0], quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getWhitePawn())[0]);
        Assertions.assertEquals(white[1], quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getWhitePawn())[1]);
    }
}
