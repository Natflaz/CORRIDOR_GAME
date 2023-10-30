package quoridor.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphTests {
    @Test
    public void testCreateShortestPath() {
        Graph graph = new Graph();
        QuoridorStageModel stageModel = mock(QuoridorStageModel.class);
        when(stageModel.getBoard()).thenReturn(mock(QuoridorBoard.class));
        when(stageModel.getBlackPawn()).thenReturn(mock(Pawn.class));
        when(stageModel.getBoard().getElementCell(stageModel.getWhitePawn())).thenReturn(new int[]{0, 0});
        int idPlayer = 1;
        when(stageModel.getBoard().noWallOrEdgeInDirection(any(Integer.class), any(Integer.class), any(Direction.class))).thenReturn(true);

        graph.calculatePathFromSourceDijkstra(stageModel, idPlayer);
        Assertions.assertEquals(10, graph.getMatrix()[5][5].getShortestPath().size());
        List<Node> nodes = graph.bestPathToWin(idPlayer);
        Assertions.assertEquals(9, nodes.size());
    }
}
