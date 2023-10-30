package quoridor.model;

import boardifier.model.GridElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuoridorBoardTests {


    @Test
    public void test_pawnDestination() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        Pawn pawn = mock(Pawn.class);
        Direction direction = Direction.NORTH;
        QuoridorBoard board = new QuoridorBoard(17, 17, model);

        board.putElement(pawn, 3, 3);
        Assertions.assertTrue(compareCoordonnee(board.pawnDestination(pawn, direction), (new int[]{1, 3})));
        Assertions.assertFalse(compareCoordonnee(board.pawnDestination(pawn, direction), (new int[]{0, 3})));
    }


    @Test
    public void test_noWallOrEdgeInDirection() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        Pawn pawn = mock(Pawn.class);
        Pawn pawn2 = mock(Pawn.class);
        Barrier barrier = mock(Barrier.class);

        QuoridorBoard board = new QuoridorBoard(17, 17, model);

        board.putElement(pawn, 0, 2);
        Assertions.assertFalse(board.noWallOrEdgeInDirection(pawn, Direction.NORTH));
        Assertions.assertTrue(board.noWallOrEdgeInDirection(pawn, Direction.SOUTH));

        board.putElement(pawn2, 2, 2);
        int[] pawnCords = board.getElementCell(pawn);
        Assertions.assertFalse(board.pawnExistsInDirection(pawnCords[0],pawnCords[1], Direction.NORTH));
        pawnCords = board.getElementCell(pawn2);
        Assertions.assertFalse(board.pawnExistsInDirection(pawnCords[0],pawnCords[1], Direction.SOUTH));

        pawnCords = board.getElementCell(pawn);
        Assertions.assertTrue(board.pawnExistsInDirection(pawnCords[0],pawnCords[1], Direction.SOUTH));
        pawnCords = board.getElementCell(pawn2);
        Assertions.assertTrue(board.pawnExistsInDirection(pawnCords[0],pawnCords[1], Direction.NORTH));

        board.putElement(barrier, 2, 3);
        Assertions.assertFalse(board.noWallOrEdgeInDirection(pawn, Direction.NORTH));
        Assertions.assertFalse(board.noWallOrEdgeInDirection(0, 2, Direction.NORTH));
    }


    @Test
    public void test_canPlaceBarrier() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        QuoridorBoard board = new QuoridorBoard(17, 17, model);
        Barrier barrier = mock(Barrier.class);

        Assertions.assertTrue(board.canPlaceBarrier(2, 3, BarrierType.VERTICAL));
        Assertions.assertFalse(board.canPlaceBarrier(5, 5, BarrierType.HORIZONTAL));
        Assertions.assertFalse(board.canPlaceBarrier(8, 18, BarrierType.HORIZONTAL));


        board.putElement(barrier, 4, 4);
        Mockito.when(barrier.getBarrierType()).thenReturn(BarrierType.HORIZONTAL);

        boolean canPlace = board.canPlaceBarrier(4, 3, BarrierType.HORIZONTAL);
        Assertions.assertFalse(canPlace);
    }

    @Test
    public void testcanPlaceBarrierWeird90degreesBug() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        QuoridorBoard board = new QuoridorBoard(0, 0, model);

        Barrier barrierV = mock(Barrier.class);
        when(barrierV.getBarrierType()).thenReturn(BarrierType.VERTICAL);

        Barrier barrierH = mock(Barrier.class);
        when(barrierH.getBarrierType()).thenReturn(BarrierType.HORIZONTAL);

        Assertions.assertTrue(board.canPlaceBarrier(1,10,BarrierType.HORIZONTAL));
        board.putElement(barrierH,1,9);

        Assertions.assertTrue(board.canPlaceBarrier(2,11,BarrierType.VERTICAL));
//        board.putElement(barrierH,0,10);
    }
    private void tryPutBarrier() {

    }

    @Test
    public void putPawnTest() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        QuoridorBoard board = new QuoridorBoard(17, 17, model);
        GridElement gridElement = mock(GridElement.class);

        Pawn pawn = mock(Pawn.class);
        Direction direction = Direction.NORTH;
        board.initializePawnPosition(pawn, direction);
        Assertions.assertTrue(board.getElement(0, board.getNbCols()/2) instanceof Pawn);
        direction = Direction.SOUTH;
        board.initializePawnPosition(pawn, direction);
        Assertions.assertTrue(board.getElement(board.getNbRows()-1, board.getNbCols()/2) instanceof Pawn);
        direction = Direction.EAST;
        board.initializePawnPosition(pawn, direction);
        Assertions.assertTrue(board.getElement(board.getNbRows()/2, board.getNbCols()-1) instanceof Pawn);
        direction = Direction.WEST;
        board.initializePawnPosition(pawn, direction);
        Assertions.assertTrue(board.getElement(board.getNbRows()/2, 0) instanceof Pawn);
    }
    private boolean compareCoordonnee(int[] tab1, int[] tab2){
        if(tab1.length != tab2.length)
            return false;
        for (int i=0; i<tab1.length;i++){
            if(tab1[i] != tab2[i]){
                return false;
            }
        }
        return true;
    }

    public void printab(int[] tab){
        for(int i : tab){
            System.out.println(i + " ");
        }
    }
}

