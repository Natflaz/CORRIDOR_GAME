package quoridor.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A node used in the Graph object used to get dijksra's shortest path.
 */
public class Node {

    private final int row;
    private final int col;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public Node(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * add relations between this node and its neighbors
     * @param stageModel Quoridor Stage Model
     * @param graph the graph
     * @return the changed graph
     */
    public Graph createGraph(QuoridorStageModel stageModel, Graph graph) {
        Node[][] matrix = graph.getMatrix();
        int rows = matrix.length;
        int cols = matrix[0].length;

        boolean north;
        boolean south;
        boolean west;
        boolean east;

        north = stageModel.getBoard().noWallOrEdgeInDirection(row, col, Direction.NORTH);
        south = stageModel.getBoard().noWallOrEdgeInDirection(row, col, Direction.SOUTH);
        west = stageModel.getBoard().noWallOrEdgeInDirection(row, col, Direction.WEST);
        east = stageModel.getBoard().noWallOrEdgeInDirection(row, col, Direction.EAST);

        if (north && row > 0) {
            Node northNode = matrix[row-1][col];
            addDestination(northNode, 1);
        }

        // Add south node
        if (south && row + 1 < rows) {
            Node southNode = matrix[row+1][col];
            addDestination(southNode, 1);
        }

        // Add west node
        if (west && col > 0) {
            Node westNode = matrix[row][col-1];
            addDestination(westNode, 1);
        }

        // Add east node
        if (east && col+1 < cols) {
            Node eastNode = matrix[row][col+1];
            addDestination(eastNode, 1);
        }

        graph.setMatrix(matrix);
        return graph;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
