package quoridor.control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;
import quoridor.view.ErrorMessage;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * The MovesVerifier class represents a utility class for verifying and validating moves in a game.
 * It provides methods to check the validity of moves and perform verification checks.
 *
 */
public class MovesVerifier {

    private Model model;
    private Controller controller;
    private QuoridorStageModel quoridorStageModel;
    private QuoridorController quoridorController;
    private QuoridorBoard quoridorBoard;

    public MovesVerifier(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
    }

    public void setModel(Model model){this.model = model;}
    public void setController(QuoridorController controller){this.controller = controller;}

    /**
     * Checks if the move is valid.
     * @param actionList the list of actions to perform
     * @return true if the move is valid, false otherwise
     */
    public boolean verify(ActionList actionList) {
        this.quoridorStageModel = (QuoridorStageModel) model.getGameStage();
        this.quoridorController = (QuoridorController) controller;
        this.quoridorBoard = quoridorStageModel.getBoard();
        int idPlayer = model.getIdPlayer();
        if (model.getIdPlayer() == QuoridorStageModel.BLACK) otherId = QuoridorStageModel.WHITE;
        GameAction gameAction = actionList.getActions().get(0).get(0);
        if (gameAction instanceof BarrierMoveAction){
            BarrierMoveAction barrierMoveAction = (BarrierMoveAction) gameAction;
            int row = barrierMoveAction.getRowDest();
            int col = barrierMoveAction.getColDest();
            BarrierType barrierType = barrierMoveAction.getBarrierType();
            if(!quoridorBoard.canPlaceBarrier(row,col,barrierType))
                return false;
            if(!testIfThereIsStillAPathAfterAction(barrierMoveAction))
                return false;
            return true;
        } else if (gameAction instanceof MoveAction) {
            MoveAction moveAction = (MoveAction) gameAction;
            Direction direction =  getMoveDirection(actionList);
            Pawn pawn;
            if(idPlayer == QuoridorStageModel.BLACK) pawn = quoridorStageModel.getBlackPawn();
            else pawn = quoridorStageModel.getWhitePawn();
            int[] dest = {moveAction.getRowDest(), moveAction.getColDest()};
            return isCaseValid(pawn, dest[0], dest[1]);
        } else {
            ErrorMessage.showAlert("you cannot move here");
            return false;
        }

    }


    /**
     * Check if a specific case is valid for the pawn to move to
     * @param p the pawn
     * @param x the x-coordinate of the case
     * @param y the y-coordinate of the case
     * @return true if the case is valid for the pawn, false otherwise
     */
    private boolean isCaseValid(Pawn p, int x, int y) {
        List<Point> validCells = quoridorBoard.computeValidCellsPawn(p);
        Point caseToCheck = new Point(x, y);
        return validCells.contains(caseToCheck);
    }

    private boolean isRightDistance(int idPlayer,MoveAction moveAction, int jump){
        int[] coords;
        if (idPlayer == QuoridorStageModel.BLACK) {
            coords = quoridorBoard.getElementCell(quoridorStageModel.getBlackPawn());
        } else {
            coords = quoridorBoard.getElementCell(quoridorStageModel.getWhitePawn());
        }
        int currentRow = coords[0];
        int currentCol = coords[1];
        int nextRow = moveAction.getRowDest();
        int nextCol = moveAction.getColDest();
        if (jump == -1)
            if ((Math.abs(currentRow - nextRow) == 1 && Math.abs(currentCol - nextCol) == 0) || ((Math.abs(currentCol - nextCol) == 1) && (Math.abs(currentRow - nextRow) == 0))) return true;
        if (jump == 1)
            if (((Math.abs(currentRow - nextRow) == 2 && Math.abs(currentCol - nextCol) == 0) || (Math.abs(currentCol - nextCol) == 2 && Math.abs(currentRow - nextRow) == 0))) return true;
        if (jump == 2){
            if (Math.abs(currentRow - nextRow) == 1 && Math.abs(currentCol - nextCol) == 1) return true;
        }
        if (jump == 3){
            if (Math.abs(currentRow - nextRow) == 1 && Math.abs(currentCol - nextCol) == 1) return true;
        }
        if (jump == 4){
            if (Math.abs(currentRow - nextRow) == 1 && Math.abs(currentCol - nextCol) == 1) return true;
        }
        return false;

    }

    private int distanceFromDestination(Pawn pawn, MoveAction moveAction) {
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        int[] coords = board.getElementCell(pawn);
        int currentRow = coords[0];
        int currentCol = coords[1];
        int nextRow = moveAction.getRowDest();
        int nextCol = moveAction.getColDest();
        int distancecol = Math.abs(currentCol - nextCol);
        int distancerow = Math.abs(currentRow - nextRow);
        int distance = distancecol + distancerow;
        return distance;
    }

    private Direction[] getfirstMoveDirectionIfMultipleDirection(MoveAction moveAction, QuoridorBoard board){
        Direction[] direction = new Direction[2];
        int idPlayer = model.getIdPlayer();
        Pawn pawn;
        if (idPlayer == QuoridorStageModel.BLACK) {
            pawn = quoridorStageModel.getBlackPawn();
        } else {
            pawn = quoridorStageModel.getWhitePawn();
        }
        Pawn otherPawn;
        if (pawn.getColor() == QuoridorStageModel.BLACK) otherPawn = quoridorStageModel.getWhitePawn();
        else otherPawn = quoridorStageModel.getBlackPawn();
        int[] cords1 = board.getElementCell(pawn);
        int[] cords2 = board.getElementCell(otherPawn);
        if(cords1[0] > cords2[0] && cords1[1] == cords2[1])
            direction[0] = Direction.NORTH;
        else if(cords1[0] < cords2[0] && cords1[1] == cords2[1])
            direction[0] = Direction.SOUTH;
        else if(cords1[0] == cords2[0] && cords1[1] < cords2[1])
            direction[0] = Direction.EAST;
        else if(cords1[0] == cords2[0] && cords1[1] > cords2[1])
            direction[0] = Direction.WEST;
        System.out.println(direction);

        int[] destmoveAction = {moveAction.getRowDest(), moveAction.getColDest()};
        if(cords2[0] > destmoveAction[0] && cords2[1] == destmoveAction[1])
            direction[1] = Direction.NORTH;
        else if(cords2[0] < destmoveAction[0] && cords2[1] == destmoveAction[1])
            direction[1] = Direction.SOUTH;
        else if(cords2[0] == destmoveAction[0] && cords2[1] < destmoveAction[1])
            direction[1] = Direction.WEST;
        else if(cords2[0] == destmoveAction[0] && cords2[1] > destmoveAction[1])
            direction[1] = Direction.EAST;

        return direction;
    }

    private Direction getMoveDirection(ActionList actionList) {
        MoveAction moveAction = (MoveAction) actionList.getActions().get(0).get(0);
        Direction direction = null;
        int idPlayer = model.getIdPlayer();
        int[] coords;
        if (idPlayer == QuoridorStageModel.BLACK) {
            coords = quoridorBoard.getElementCell(quoridorStageModel.getBlackPawn());
        } else {
            coords = quoridorBoard.getElementCell(quoridorStageModel.getWhitePawn());
        }
        int currentRow = coords[0];
        int currentCol = coords[1];
        int nextRow = moveAction.getRowDest();
        int nextCol = moveAction.getColDest();

        if (currentRow < nextRow && quoridorBoard.noWallOrEdgeInDirection(currentRow, currentCol, Direction.SOUTH)) {
            direction = Direction.SOUTH;
        } else if (currentRow > nextRow && quoridorBoard.noWallOrEdgeInDirection(currentRow, currentCol, Direction.NORTH)) {
            direction = Direction.NORTH;
        } else if (currentCol < nextCol && quoridorBoard.noWallOrEdgeInDirection(currentRow, currentCol, Direction.EAST)) {
            direction = Direction.EAST;
        } else if (currentCol > nextCol && quoridorBoard.noWallOrEdgeInDirection(currentRow, currentCol, Direction.WEST)) {
            direction = Direction.WEST;
        }

        return direction;
    }

    private boolean hasPawn(Direction direction, Pawn pawn, QuoridorBoard board){
        int idPlayer = model.getIdPlayer();
        Pawn otherPawn;
        if (pawn.getColor() == QuoridorStageModel.BLACK) otherPawn = quoridorStageModel.getWhitePawn();
        else otherPawn = quoridorStageModel.getBlackPawn();
        int[] cords1 = board.getElementCell(pawn);
        int[] cords2 = board.getElementCell(otherPawn);
        if(cords1[0] > cords2[0] && cords1[1] == cords2[1] && direction == Direction.NORTH && cords1[0]-cords2[0] == 1)
            return true;
        else if(cords1[0] < cords2[0] && cords1[1] == cords2[1] && direction == Direction.SOUTH && cords2[0]-cords1[0] == 1)
            return true;
        else if(cords1[0] == cords2[0] && cords1[1] < cords2[1] && direction == Direction.EAST && cords2[1]-cords1[1] == 1)
            return true;
        else if(cords1[0] == cords2[0] && cords1[1] > cords2[1] && direction == Direction.WEST && cords1[1]-cords2[1] == 1)
            return true;
        return false;
    }

    /**
     * this function allows you to know if you can or need to jump
     * @param pawn the pawn that may jump
     * @param direction in which direction it would jump
     * @return if the pawn need to jump and return where a number
     * the number must be -1, 1, 2, 3, 4
     * -1 = the pawn cannot jump
     * 1 the pawn can jump above the other pawn
     * 2 the pawn can jump on the left or on the right
     * 3 the pawn can jump on the left
     * 4 the pawn can jump on the right
     */
    private int canOrNeedToJump (Pawn pawn, Direction direction, MoveAction moveAction) {
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        Pawn otherPawn;
        if (pawn.getColor() == QuoridorStageModel.BLACK) otherPawn = quoridorStageModel.getWhitePawn();
        else otherPawn = quoridorStageModel.getBlackPawn();
        int[] dest = board.pawnDestination(pawn, direction);
        if (!board.pawnExistsInDirection(pawn, direction) && distanceFromDestination(pawn, moveAction) == 1) {
            return -1;
        } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], direction) && board.pawnExistsInDirection(pawn, direction) && board.pawnExistsInDirection(pawn, direction)) {
            return 1;
        } else {
            Direction firstDirection = getfirstMoveDirectionIfMultipleDirection(moveAction, board)[0];
            Direction secondDirection = getfirstMoveDirectionIfMultipleDirection(moveAction, board)[1];
            if (firstDirection == null || secondDirection == null) return -1;
            int destPawn[] = board.pawnDestination(pawn, firstDirection);

//            if (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, true)) && board.pawnExistsInDirection(pawn, firstDirection) && (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, false)) && board.pawnExistsInDirection(pawn, firstDirection))) {
//                return 2;
//            } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, true)) && !board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, false)) && board.pawnExistsInDirection(pawn, firstDirection)) {
//                return 3;
//            } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, false)) && !board.noWallOrEdgeInDirection(dest[0], dest[1], turn(firstDirection, true)) && board.pawnExistsInDirection(pawn, firstDirection)) {
//                return 4;
            if (false) {
                return 2;
            } else if (board.noWallOrEdgeInDirection(pawn, firstDirection) && hasPawn(firstDirection, pawn, board) && !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], firstDirection) && !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) && board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], secondDirection) && secondDirection == turn(firstDirection, false)) {
                return 3;
            } else if (board.noWallOrEdgeInDirection(pawn, firstDirection) && hasPawn(firstDirection, pawn, board) && !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], firstDirection) && !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) && board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], secondDirection) && secondDirection == turn(firstDirection, true)) {
                return 4;
            } else {
                System.out.println("black :" + Arrays.toString(board.getElementCell(quoridorStageModel.getBlackPawn())));
                System.out.println("white :" + Arrays.toString(board.getElementCell(quoridorStageModel.getWhitePawn())));
                System.out.println("gauche " + board.noWallOrEdgeInDirection(pawn, firstDirection) + hasPawn(firstDirection, pawn, board) + !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], firstDirection) + !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) + board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) + firstDirection + secondDirection + turn(firstDirection, false));
                System.out.println("droite " + board.noWallOrEdgeInDirection(pawn, firstDirection) + hasPawn(firstDirection, pawn, board) + !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], firstDirection) + !board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) + board.noWallOrEdgeInDirection(destPawn[0],destPawn[1], otherDirection(secondDirection)) + firstDirection + secondDirection + turn(firstDirection, true));
                return -1;
            }
        }
    }

    private Direction otherDirection(Direction direction) {
        if (direction == Direction.NORTH) return Direction.SOUTH;
        else if (direction == Direction.SOUTH) return Direction.NORTH;
        else if (direction == Direction.EAST) return Direction.WEST;
        else if (direction == Direction.WEST) return Direction.EAST;
        return null;
    }


    /**
     * still WIP
     * @param pawn
     * @param direction
     * @param jump
     * @return
     */
    private int[] pawnJump (Pawn pawn, Direction direction,int jump){
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        int[] firstjump = board.pawnDestination(pawn, direction);
        if (jump == 1) {
            return board.pawnDestination(firstjump[0], firstjump[1], direction);
        }
        if (jump == 3) {

            return board.pawnDestination(firstjump[0], firstjump[1], turn(direction, true));
        }
        if (jump == 4) {
            return board.pawnDestination(firstjump[0], firstjump[1], turn(direction, false));
        }
        return null;
    }
    private int[] pawnJump (Pawn pawn, Direction direction, Direction secondDirection,int jump){
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        int[] firstjump = board.pawnDestination(pawn, direction);
        return board.pawnDestination(firstjump[0], firstjump[1], secondDirection);
    }


    private Direction turn (Direction direction,boolean isLeft){
        Direction[] values = Direction.values();
        int index = direction.ordinal();
        // Calculate the new index based on the boolean parameter
        int offset = isLeft ? 1 : -1;
        index = (index + offset + values.length) % values.length;
        return values[index];
    }

    private boolean testIfThereIsStillAPathAfterAction (BarrierMoveAction BarrierAction){
        PiecesRecorder defaultpos = new PiecesRecorder(((QuoridorStageModel) model.getGameStage()));
        defaultpos.takeSnapshot();
        BarrierAction.execute();
        Graph graph = new Graph();
        graph.calculatePathFromSourceDijkstra((QuoridorStageModel) model.getGameStage(), QuoridorStageModel.WHITE);
        List<Node> nodes = graph.bestPathToWin(QuoridorStageModel.WHITE);
        graph.calculatePathFromSourceDijkstra((QuoridorStageModel) model.getGameStage(), QuoridorStageModel.BLACK);
        List<Node> nodes2 = graph.bestPathToWin(QuoridorStageModel.BLACK);
        defaultpos.revertToSnapshot();
        return nodes.size() != 0 && nodes2.size() != 0;
    }

    private Direction getSecondDirectionFromPawnInputLine(String inputLine) {
        return getDirectionFromPawnInputLine(inputLine.substring(1));
    }

    private Direction getDirectionFromPawnInputLine(String inputLine) {
        char dirChar = Character.toUpperCase(inputLine.charAt(1));
        if(dirChar == 'N')
            return Direction.NORTH;
        if(dirChar == 'S')
            return  Direction.SOUTH;
        if(dirChar == 'E')
            return Direction.EAST;
        if(dirChar == 'W')
            return Direction.WEST;
        throw  new IllegalArgumentException(dirChar+" is not a valid direction char");
    }

    private static int letterToNumber(char letter) {
        letter = Character.toLowerCase(letter);

        return letter - 'a' + 1;
    }
}

