package quoridor.model;

import boardifier.model.GameStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class QuoridorBarrierPotTests {
    @Test
    void simplePlacementTest()
    {
        GameStageModel gameStageModel = mock(GameStageModel.class);
        QuoridorBarrierPot pot = new QuoridorBarrierPot(0,0,gameStageModel);
        Barrier barrier = mock(Barrier.class);

        Assertions.assertFalse(pot.isPotFull());
        Assertions.assertEquals(0, pot.numberOfBarriers());
        Assertions.assertNull(pot.getBarrier());

        pot.putBarrier(barrier);

        Assertions.assertEquals(1, pot.numberOfBarriers());
        Assertions.assertSame(pot.getBarrier(), barrier);
        Assertions.assertFalse(pot.isPotFull());

        verify(barrier, times(1)).setBarrierType(BarrierType.POT);
        verify(barrier, times(1)).setGrid(pot);
    }

    @Test
    void barrierCount11()
    {
        QuoridorBoard board = mock(QuoridorBoard.class);

        GameStageModel gameStageModel = mock(GameStageModel.class);
        QuoridorBarrierPot pot = new QuoridorBarrierPot(0,0,gameStageModel);
        Barrier[] barriers = new Barrier[10];

        for (int i = 0; i < barriers.length; i++) {
            barriers[i] = mock(Barrier.class);
            pot.putBarrier(barriers[i]);
        }
        Assertions.assertEquals(pot.numberOfBarriers(),10);
        Assertions.assertTrue(pot.isPotFull());

        Barrier tooMuch = mock(Barrier.class);

        boolean exceptionCaught = false;
        try {
            pot.putBarrier(tooMuch);
        }
        catch (Exception e) {
            exceptionCaught = true;
        }

        Assertions.assertEquals(pot.numberOfBarriers(),10);
        Assertions.assertTrue(pot.isPotFull());
        Assertions.assertTrue(exceptionCaught);
    }

}

