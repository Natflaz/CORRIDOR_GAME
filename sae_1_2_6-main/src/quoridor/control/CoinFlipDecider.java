package quoridor.control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The CoinFlipDecider class represents a decision-making entity that uses a coin flip to make decisions.
 * It extends the Decider class and provides a simple mechanism for decision-making based on random chance and the dijstra algorithm.
 *
 * @see Decider
 */
public class CoinFlipDecider extends Decider {
    //TODO: DO NOT FORGET TO ADD A COINFLIP TO THE GUI, the coin could fall on the side and then explode

    private GameStageModel gameStageModel;
    private int playerID;
    private final CoinFlipper flipper;

    private QuoridorStageModel quoridorStageModel;
    private Graph graph;

    private MovesVerifier movesVerifier;


    public CoinFlipDecider(Model model, Controller controller, CoinFlipper flipper, Graph graph)
    {
        super(model, controller);
        this.flipper = flipper;
        this.gameStageModel = model.getGameStage();
        this.quoridorStageModel = ((QuoridorStageModel)model.getGameStage());
        this.graph =graph;
    }

    /**
     *this method return an ActionList containing a MoveAction that move the pawn of the AI or a BarrierMoveAction that place a barrier
     *  @return
     */
    @Override
    public ActionList decide() {
        this.playerID = model.getIdPlayer();
        ActionList actions = new ActionList(true);
        List<ActionList> list = new ArrayList<>();
        this.gameStageModel = model.getGameStage();
        this.quoridorStageModel = ((QuoridorStageModel)model.getGameStage());

        if (flipper.flipCoin() && !quoridorStageModel.isPotEmpty()) {
            int[] dest = placeWall();
            if (dest == null){
                GameAction move = PawnAction();
                actions.addSingleAction(move);
                return actions;
            }
            Barrier barrier = playerID == 0 ? ((QuoridorStageModel)gameStageModel).getBlackBarrierToPlay() : ((QuoridorStageModel)gameStageModel).getWhiteBarrierToPlay();
            if (((QuoridorStageModel)gameStageModel).getBoard().canPlaceBarrier(dest[0], dest[1], BarrierType.HORIZONTAL)) {
                BarrierMoveAction barrierAction = new BarrierMoveAction(model, barrier, "quoridorboard", dest[0], dest[1], BarrierType.HORIZONTAL);
                if(((QuoridorController)control).testIfThereIsStillAPathAfterAction(barrierAction))
                actions.addSingleAction(barrierAction);
            }else if (((QuoridorStageModel)gameStageModel).getBoard().canPlaceBarrier(dest[0], dest[1]+2, BarrierType.HORIZONTAL)){
                BarrierMoveAction barrierAction = new BarrierMoveAction(model, barrier, "quoridorboard", dest[0], dest[1]+2,BarrierType.HORIZONTAL);
                if(((QuoridorController)control).testIfThereIsStillAPathAfterAction(barrierAction))
                actions.addSingleAction(barrierAction);
            }else if (((QuoridorStageModel)gameStageModel).getBoard().canPlaceBarrier(dest[0], dest[1], BarrierType.VERTICAL)) {
                BarrierMoveAction barrierAction = new BarrierMoveAction(model, barrier, "quoridorboard", dest[0], dest[1], BarrierType.VERTICAL);
                if(((QuoridorController)control).testIfThereIsStillAPathAfterAction(barrierAction))
                actions.addSingleAction(barrierAction);
            }else if (((QuoridorStageModel)gameStageModel).getBoard().canPlaceBarrier(dest[0]+2, dest[1], BarrierType.VERTICAL)) {
                BarrierMoveAction barrierAction = new BarrierMoveAction(model, barrier, "quoridorboard", dest[0] + 2, dest[1], BarrierType.VERTICAL);
                if(((QuoridorController)control).testIfThereIsStillAPathAfterAction(barrierAction))
                actions.addSingleAction(barrierAction);
            }else {
                GameAction move = PawnAction();
                actions.addSingleAction(move);}
        } else {
            GameAction move = PawnAction();
            actions.addSingleAction(move);
        }
        if(actions.getActions().isEmpty()){
            GameAction move = PawnAction();
            actions.addSingleAction(move);
        }
        return actions;
    }

    /**
     * this method return the coordinates of a wall so that it can be placed but in some cases it can return the position of a pawn
     * @return int[] wall position || int[] pawn position || null (null will still move a pawn)
     */
    private int[] placeWall() {
        //1 = horizontal , 2 = vertical
        QuoridorStageModel quoridorStageModel = (QuoridorStageModel) gameStageModel;
        QuoridorBoard board = (quoridorStageModel.getBoard());
        int[] coords;
        int otherID;
        if (playerID == QuoridorStageModel.WHITE) {
            coords = board.getElementCell(quoridorStageModel.getBlackPawn());
            otherID = QuoridorStageModel.BLACK;
        }
        else {
            coords = board.getElementCell(quoridorStageModel.getWhitePawn());
            otherID = QuoridorStageModel.WHITE;
        }
        // Determine the best move that the opponent can make
        graph.calculatePathFromSourceDijkstra(quoridorStageModel, otherID);
        List<Node> lNodes = graph.bestPathToWin(otherID);
        Node nextNode;
        do {
            //get the next node
            try {
                nextNode = lNodes.get(1);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
            int[] nextPos = {nextNode.getRow(), nextNode.getCol()};
            if (nextPos[1] < coords[1]) {
                if (board.noWallOrEdgeInDirection(coords[0], coords[1], Direction.WEST))
                    return new int[]{(coords[0] ), (coords[1] - 1)};
            }
            else if (nextPos[1] > coords[1]) {
                if (board.noWallOrEdgeInDirection(coords[0], coords[1], Direction.EAST))
                    return new int[]{(coords[0] ), (coords[1])};
            }
            else if (nextPos[0] < coords[0]) {
                if (board.noWallOrEdgeInDirection(coords[0], coords[1], Direction.NORTH))
                    return new int[]{(coords[0] - 1 ), (coords[1] - 1)};
            }
            else if (nextPos[0] > coords[0])  {
                if (board.noWallOrEdgeInDirection(coords[0], coords[1], Direction.SOUTH))
                    return new int[]{(coords[0]), (coords[1])};
            }
            else{
                lNodes.remove(1);
            }
        } while(lNodes.size() != 0);
        return null;
    }


    /**
     * This method return the next position of a pawn
     * @return int[] position
     */
    private int[] movePawn() {
        // Get the current state of the game
        QuoridorStageModel quoridorStageModel = (QuoridorStageModel) gameStageModel;
        QuoridorBoard board = (quoridorStageModel.getBoard());
        int[] coords = board.getElementCell(quoridorStageModel.getBlackPawn());
        if (playerID != 0) coords = board.getElementCell(((QuoridorStageModel) gameStageModel).getWhitePawn());
        // Determine the next direction to move the pawn in
        graph.calculatePathFromSourceDijkstra(quoridorStageModel, playerID);
        List<Node> lNodes = graph.bestPathToWin(playerID);
        Node nextNode;
        int[] nextPos;
        try {
            nextNode = lNodes.get(1);
        } catch (IndexOutOfBoundsException e) {
            nextNode = lNodes.get(0);
            if (playerID == QuoridorStageModel.BLACK)
                nextPos = new int[]{nextNode.getRow() - 2, nextNode.getCol()};
            else nextPos = new int[]{nextNode.getRow() + 2, nextNode.getCol()};
            return nextPos;
        }
        nextPos = new int[]{nextNode.getRow(), nextNode.getCol()};

        int[] coord;
        if (playerID == QuoridorStageModel.BLACK) coord = quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getWhitePawn());
        else coord = quoridorStageModel.getBoard().getElementCell(quoridorStageModel.getBlackPawn());
        int row = coord[0];
        int col = coord[1];
        int[] positionPawn = new int[] {row, col};
        if (Arrays.equals((positionPawn), nextPos)){
            try{
                nextNode = lNodes.get(2);
                nextPos = new int[]{nextNode.getRow(), nextNode.getCol()};
            }catch (Exception e){
                if ((coords[0] == 1 && playerID == QuoridorStageModel.BLACK)) {
                    if (board.getElementCell(quoridorStageModel.getWhitePawn())[0] == 0 && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.EAST))
                        nextPos = new int[]{nextNode.getRow(), nextNode.getCol() + 2};
                    else if (board.getElementCell(quoridorStageModel.getWhitePawn())[0] == 0 && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.WEST))
                        nextPos = new int[]{nextNode.getRow(), nextNode.getCol() - 2};
                    return nextPos;
                } else if (coords[0] == 14 && playerID == QuoridorStageModel.WHITE) {
                    if (board.getElementCell(quoridorStageModel.getBlackPawn())[0] == 16 && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.EAST))
                        nextPos = new int[]{nextNode.getRow(), nextNode.getCol() + 2};
                    else if (board.getElementCell(quoridorStageModel.getBlackPawn())[0] == 16 && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.WEST))
                        nextPos = new int[]{nextNode.getRow(), nextNode.getCol() - 2};
                    return nextPos;
                }

            }
            if ((positionPawn[0] == 1 && playerID == QuoridorStageModel.BLACK)){
                if ((nextPos[1] > coords[1]) && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.EAST))
                    nextPos = new int[]{nextNode.getRow()+2, nextNode.getCol() + 2};
                else if ((nextPos[1] < coords[1]) && board.noWallOrEdgeInDirection(nextPos[0], nextPos[1], Direction.WEST))
                    nextPos = new int[]{nextNode.getRow()+2, nextNode.getCol() - 2};
                else nextPos = new int[]{nextNode.getRow(), nextNode.getCol()};

            }else if (positionPawn[0] == 14 && playerID == QuoridorStageModel.WHITE) {
                if ((nextPos[1] > coords[1]) && board.noWallOrEdgeInDirection(nextPos[0]-2, nextPos[1], Direction.EAST))
                    nextPos = new int[]{nextNode.getRow()-2, nextNode.getCol() + 2};
                else if ((nextPos[1] < coords[1]) && board.noWallOrEdgeInDirection(nextPos[0]-2, nextPos[1], Direction.WEST))
                    nextPos = new int[]{nextNode.getRow()-2, nextNode.getCol() - 2};
                else nextPos = new int[]{nextNode.getRow(), nextNode.getCol()};
            }
        }
        return nextPos;
    }

    /**
     * this code will return a GameAction that will move the pawn
     * @return
     */
    private GameAction PawnAction(){
        int[] dest = movePawn();
        GameAction move;
        if (playerID == QuoridorStageModel.WHITE)
            move = new MoveAction(model, ((QuoridorStageModel) gameStageModel).getWhitePawn(), "quoridorboard", dest[0], dest[1]);
        else
            move = new MoveAction(model, ((QuoridorStageModel) gameStageModel).getBlackPawn(), "quoridorboard", dest[0], dest[1]);
        return move;
    }
}


