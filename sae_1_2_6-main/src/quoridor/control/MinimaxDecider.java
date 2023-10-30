package quoridor.control;

import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

import java.util.ArrayList;
import java.util.List;

/**
 * The MinimaxDecider class represents a decision-making entity that utilizes the minimax algorithm for decision-making.
 * It extends the Decider class and provides a strategy for making optimal decisions in games with perfect information.
 *
 * @see Decider
 */
public class MinimaxDecider extends Decider {
    /*
     * Minimax iterative :
     * evaluates all possibles move
     * calculates the best
     */

    QuoridorStageModel stageModel;

    public MinimaxDecider(Model model, QuoridorController controller) {
        super(model, controller);
        this.stageModel = null;
    }

    /**
     * uses an implementation of the minmax algorithm to choose what moves to play
     * @return the chosen move(s)
     */
    @Override
    public ActionList decide() {
        stageModel = (QuoridorStageModel) model.getGameStage();
        int bestValue = Integer.MIN_VALUE;
        ActionList bestMove = null;
        ((QuoridorController) control).setStopUpdatingView(true);
        for (ActionList action : possibleMoves(model.getIdPlayer())) {
            PiecesRecorder defaultState = new PiecesRecorder(stageModel);
            defaultState.takeSnapshot(); // TODO : there is a possible optimisation : we are taking the same snapshot over and over again
            action.getActions().get(0).get(0).execute();
            int res =  minimax(2, true, model.getIdPlayer());
            if (res > bestValue || bestMove == null) {
                bestValue = res;
                bestMove = action;
            }
            defaultState.revertToSnapshot();
        }
        ActionList actionList = new ActionList(true);
        MoveAction move = null;
        int row, col;
        if (bestMove == null) {
            return null;
        }
        if (bestMove.getActions().get(0).get(0) instanceof MoveAction) {
            row = ((MoveAction) bestMove.getActions().get(0).get(0)).getRowDest();
            col = ((MoveAction) bestMove.getActions().get(0).get(0)).getColDest();
            GameElement element = bestMove.getActions().get(0).get(0).getElement();
            move = new MoveAction(model, element, "quoridorboard", row, col);
            if (bestMove.getActions().get(0).get(0) instanceof BarrierMoveAction) {
                Barrier barrier = (Barrier) bestMove.getActions().get(0).get(0).getElement();
                BarrierMoveAction barrierAction = new BarrierMoveAction(model, barrier, "quoridorboard", row, col, ((BarrierMoveAction) bestMove.getActions().get(0).get(0)).getBarrierType());
                actionList.addSingleAction(barrierAction);
            } else {
                actionList.addSingleAction(move);
            }
        }
        ((QuoridorController) control).setStopUpdatingView(false);
        return actionList;
    }

    /**
     * @return the best move (best value, best move)
     */
    private int minimax(int depth, boolean maximizingPlayer, int currentPlayer) {
        PiecesRecorder defaultState = new PiecesRecorder(stageModel);
        defaultState.takeSnapshot();

        int adversary = (currentPlayer == QuoridorStageModel.BLACK) ? QuoridorStageModel.WHITE : QuoridorStageModel.BLACK;
        int bestValue = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        Pawn whitePawn = stageModel.getWhitePawn();
        Pawn blackPawn = stageModel.getBlackPawn();

        if (stageModel.getBoard().isOnEdge(whitePawn, Direction.SOUTH)) {
            bestValue =  (currentPlayer == Pawn.PAWN_WHITE) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } if(stageModel.getBoard().isOnEdge(blackPawn, Direction.NORTH)){
            bestValue =  (currentPlayer == Pawn.PAWN_BLACK) ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        if (depth == 0) {
            int eval = evaluate(currentPlayer);
            bestValue = Math.max(bestValue, maximizingPlayer ? eval : -eval);
        } else {
            int value;
            for (ActionList move : possibleMoves(currentPlayer)) {
                List<List<GameAction>> lists = move.getActions();
                for (List<GameAction> list : lists) {
                    for (GameAction action : list) {
                        if (action.getElement() == null) {
                            continue;
                        }
                        if (action instanceof BarrierMoveAction) {
                            if (((BarrierMoveAction) action).getBarrierType() == null) {
                                continue;
                            }
                        }
                        action.execute();
                    }
                }
                value = minimax(depth - 1, !maximizingPlayer, adversary);
                if ((bestValue < value && maximizingPlayer) || (bestValue > value && !maximizingPlayer)) {
                    bestValue = value;
                }
                if (maximizingPlayer && bestValue == Integer.MAX_VALUE) {
                    return bestValue;
                } else if (!maximizingPlayer && bestValue == Integer.MIN_VALUE) {
                    return bestValue;
                }
            }
        }
        defaultState.revertToSnapshot();
        return bestValue;
    }

    /**
     * Evaluate the current state of the game
     * @return the evaluation
     */
    private int evaluate(int idPlayer) {
        Graph graph = new Graph();
        graph.calculatePathFromSourceDijkstra(stageModel, idPlayer);
        List<Node> playerPath = graph.bestPathToWin(model.getIdPlayer());
        graph.calculatePathFromSourceDijkstra(stageModel, idPlayer == QuoridorStageModel.WHITE ? QuoridorStageModel.BLACK : QuoridorStageModel.WHITE);
        List<Node> opponentPath = graph.bestPathToWin(model.getIdPlayer());
        // int nbWallsPlayerDif;
        // if (idPlayer == 0) {//black
        //    nbWallsPlayerDif = stageModel.getBlackBarrierPot().numberOfBarriers() - stageModel.getWhiteBarrierPot().numberOfBarriers();
        // } else {//white
        //    nbWallsPlayerDif = - stageModel.getBlackBarrierPot().numberOfBarriers() + stageModel.getWhiteBarrierPot().numberOfBarriers();
        // }
        return (opponentPath.size() - playerPath.size());
    }

    /**
     * Returns all the possible moves in the game
     * @param idPlayer the player for whom we will compute the moves
     */
    private List<ActionList> possibleMoves(int idPlayer) {
        List<ActionList> list = new ArrayList<>();
        Graph graph = new Graph();
        graph.calculatePathFromSourceDijkstra(stageModel, idPlayer);
        List<Node> playerPath = graph.bestPathToWin(idPlayer);
        if (playerPath.size() == 0) {
            return list;
        }
        int row;
        int col;
        int r = 1;
        try {
            row = playerPath.get(r).getRow();
            col = playerPath.get(r).getCol();
        } catch (IndexOutOfBoundsException e) {
            r-=1;
            row = playerPath.get(r).getRow();
            col = playerPath.get(r).getCol();
        }

        if (stageModel.getBoard().getElement(row, col) != null) {
            int[] coords = stageModel.getBoard().getElementCell(idPlayer == QuoridorStageModel.BLACK ? stageModel.getBlackPawn() : stageModel.getWhitePawn());
//            if (coords[0] == row && coords[1] == col) {
                try {
                    r+=1;
                    row = playerPath.get(r).getRow();
                    col = playerPath.get(r).getCol();
                } catch (IndexOutOfBoundsException e) {
                    QuoridorBoard board = stageModel.getBoard();
                    if ((row == 1 && idPlayer == QuoridorStageModel.BLACK)) {
                        if ((col > coords[1]) && board.noWallOrEdgeInDirection(row, col, Direction.EAST)) {
                            row += 2;
                            col += 2;
                        } else if ((col < coords[1]) && board.noWallOrEdgeInDirection(row, col, Direction.WEST)) {
                            row += 2;
                            col -= 2;
                        }
                    } else if (col == 14 && idPlayer == QuoridorStageModel.WHITE) {
                        if ((col > coords[1]) && board.noWallOrEdgeInDirection(row-2, col, Direction.EAST)) {
                            row -= 2;
                            col += 2;
                        } else if ((col < coords[1]) && board.noWallOrEdgeInDirection(row-2, col, Direction.WEST)) {
                            row -= 2;
                            col -= 2;
                        }
                    }
                }
//            }
        }
        ActionList actionList = new ActionList(false);
        if (idPlayer == QuoridorStageModel.BLACK) {
            actionList.addSingleAction(new MoveAction(model, stageModel.getBlackPawn(), "quoridorboard", row, col));
        } else {
            actionList.addSingleAction(new MoveAction(model, stageModel.getWhitePawn(), "quoridorboard", row, col));
        }
        list.add(actionList);
        list.addAll(computeBarrierPlacement(idPlayer));
        return list;
    }

    /**
     * Returns all the possible moves for a barrier
     * It does not check for all possibles postions because it would be too long
     * @param idPlayer the id of the player
     * @return the list of possible moves
     */
    private List<ActionList> computeBarrierPlacement(int idPlayer) {
        List<ActionList> list = new ArrayList<>();

        String gridDest = "quoridorboard";
        Barrier barrierToPlay = idPlayer == QuoridorStageModel.BLACK ? stageModel.getBlackBarrierToPlay() : stageModel.getWhiteBarrierToPlay();
        Graph graph = new Graph();
        graph.calculatePathFromSourceDijkstra(stageModel, idPlayer == QuoridorStageModel.BLACK ? QuoridorStageModel.WHITE : QuoridorStageModel.BLACK);
        List<Node> nodes = graph.bestPathToWin(idPlayer == QuoridorStageModel.BLACK ? QuoridorStageModel.WHITE : QuoridorStageModel.BLACK);
        for (int i = 1 ; i < nodes.size() - 1 ; i++) {
            for (int row = -1 ; row < 1 ; row++) {
                for (int col = -1 ; col < 1 ; col++) {
                    if (stageModel.getBoard().canPlaceBarrier(row + nodes.get(i).getRow(), col + nodes.get(i).getCol(), BarrierType.HORIZONTAL)) {
                        ActionList ls = new ActionList(false);
                        ls.addSingleAction(new BarrierMoveAction(model, barrierToPlay, gridDest, row + nodes.get(i).getRow(), col + nodes.get(i).getCol(), BarrierType.HORIZONTAL));
                        list.add(ls);
                    }
                    if (stageModel.getBoard().canPlaceBarrier(row + nodes.get(i).getRow(), col + nodes.get(i).getCol(), BarrierType.VERTICAL)) {
                        ActionList ls = new ActionList(false);
                        ls.addSingleAction(new BarrierMoveAction(model, barrierToPlay, gridDest, row + nodes.get(i).getRow(), col + nodes.get(i).getCol(), BarrierType.VERTICAL));
                        list.add(ls);
                    }
                }
            }
        }
        return list;
    }
}
