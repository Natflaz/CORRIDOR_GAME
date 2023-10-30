package quoridor.model;

import boardifier.model.Model;

import java.util.*;

/**
 * A representation of a graph, it uses a matrix of nodes
 * to get dijksra's shortest path :
 * create new instance of graph,
 * {@link #calculatePathFromSourceDijkstra(QuoridorStageModel, int)}
 * {@link Node#getShortestPath()} => node = the destination, source = pos of the player
 * or
 * {@link #bestPathToWinOpponent(QuoridorStageModel, Model)} to get the best path to win (List<Node>)
 */
public class Graph {
    private int rows;
    private int cols;
    private Node[][] matrix;
    
    public Graph() {
        rows = 9;
        cols = 9;
        matrix = new Node[rows][cols];
        init();
    }

    Graph(int rows, int cols, Node[][] matrix) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = matrix;
    }

    /**
     * used to init each node
     * called at construction
     */
    private void init() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                matrix[row][col] = new Node(row, col);
            }
        }
    }

    /**
     * used to get every adjacent node of every node
     * @param stageModel
     * @param graph
     */
    private void createRelation(QuoridorStageModel stageModel, Graph graph) {
        for (int row = 0 ; row < rows ; row++) {
            for (int col = 0 ; col < cols ; col++)  {
                matrix[row][col].createGraph(stageModel, this);
            }
        }
    }

    /**
     * get the lowest distance node from a set of nodes
     * @param unsettledNodes set of nodes that wasn't visited yet
     * @return the lowest distance node
     */
    private static Node getLowestDistanceNode(Set<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    /**
     * calculate the minimum distance and apply it to the node between the source and the evaluation node
     * @param evalutaionNode
     * @param edgeWeight
     * @param sourceNode
     */
    private static void calculateMinimumDistance(Node evalutaionNode, Integer edgeWeight, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeight < evalutaionNode.getDistance()) {
            evalutaionNode.setDistance(sourceDistance + edgeWeight);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evalutaionNode.setShortestPath(shortestPath);
        }
    }

    /**
     * calculate the shortest path from source to every node
     * @param stageModel
     * @param playerId
     */
    public void calculatePathFromSourceDijkstra(QuoridorStageModel stageModel, int playerId) {
        init();
        createRelation(stageModel, this);
        int row;
        int col;
        int[] coord;
        if (playerId == QuoridorStageModel.BLACK) {
            coord = stageModel.getBoard().getElementCell(stageModel.getBlackPawn());
        } else if (playerId == QuoridorStageModel.WHITE) {
            coord = stageModel.getBoard().getElementCell(stageModel.getWhitePawn());
        } else {
            return;
        }
        row = coord[0];
        col = coord[1];
        Node source = matrix[row][col];
        source.setDistance(0);
        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();
        unsettledNodes.add(source);
        while(unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Node, Integer> adjacentPair : currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacentPair.getKey();
                Integer edgeWeight = adjacentPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
//        printMatrix();
    }

    private void printMatrix() {
        for (int row = 0 ; row < rows ; row++) {
            for (int col = 0 ; col < cols ; col++)  {
                System.out.print(matrix[row][col].getAdjacentNodes() + ", ");
            }
            System.out.println();
        }
    }

    /**
     * Get the best path to win
     * @return
     */
    public List<Node> bestPathToWin(int playerId) {
        List<Node> current;
        List<Node> best = new ArrayList<>();
        int winningRow = playerId == 0 ? 0 : 8;
        for (int col = 0; col < cols; col++) {
            current = matrix[winningRow][col].getShortestPath();
            if (current.size() < best.size() || best.size() == 0) {
                current.add(matrix[winningRow][col]);
                best = current;
            }
        }
        if (best.size() == 1 && best.get(0).getDistance() == Integer.MAX_VALUE){
            return new ArrayList<>();
        }
        else {
            return best;
        }
    }

    /**
     * get the best path to win for the opponent
     * @param stageModel
     * @param model
     * @return
     */
    public List<Node> bestPathToWinOpponent(QuoridorStageModel stageModel, Model model) {
        List<Node> current = new ArrayList<>();
        List<Node> best = new ArrayList<>();
        int winningRow = model.getIdPlayer() == 0 ? 8 : 0;
        for (int i = 0; i < 9; i++) {
            current = matrix[winningRow][i].getShortestPath();
            if (current.size() < best.size() || best.size() == 0) {
                best = current;
            }
        }
        return best;
    }

    public Node[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(Node[][] matrix) {
        this.matrix = matrix;
    }
}
