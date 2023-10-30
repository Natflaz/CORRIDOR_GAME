package quoridor.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NodeTests {
    @Test
    public void testCreateGraph() {
        QuoridorStageModel model = mock(QuoridorStageModel.class);
        when(model.getBoard()).thenReturn(mock(QuoridorBoard.class));
        when(model.getBoard().noWallOrEdgeInDirection(0, 0, Direction.NORTH)).thenReturn(false);
        when(model.getBoard().noWallOrEdgeInDirection(0, 0, Direction.SOUTH)).thenReturn(true);
        when(model.getBoard().noWallOrEdgeInDirection(0, 0, Direction.EAST)).thenReturn(false);
        when(model.getBoard().noWallOrEdgeInDirection(0, 0, Direction.WEST)).thenReturn(false);
        Graph graph = mock(Graph.class);
        Node[][] matrix = new Node[2][2];
        matrix[0][0] = new Node(0, 0);
        matrix[1][0] = new Node(1, 0);
        when(graph.getMatrix()).thenReturn(matrix);
        Node node = new Node(0, 0);
        node.createGraph(model, graph);
        Assertions.assertEquals(graph.getMatrix()[1][0].getRow(), 1);
        Assertions.assertEquals(graph.getMatrix()[1][0].getCol(), 0);
        Node[][] matrix2 = graph.getMatrix();
        Map<Node, Integer> nodes = node.getAdjacentNodes();
        int size = nodes.size();
        Assertions.assertEquals(size, 1);
    }
}
