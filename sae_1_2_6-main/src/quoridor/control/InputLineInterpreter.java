package quoridor.control;

import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

/**
 * previously named HumanDecider, but it's Decider extension was removed
 */
public class InputLineInterpreter {

    private final Model model;

    public static class GameStopRequestedException extends Exception{}
    public static class InvalidInputLineException extends Exception{}
    public static class InvalidMoveException extends Exception{}
    private final MovesVerifier movesVerifier;
    public InputLineInterpreter(Model model, MovesVerifier movesVerifier)
    {
        this.model = model;
        this.movesVerifier = movesVerifier;
    }
//    public ActionList decide() {
//
//        Player currentPlayer = model.getCurrentPlayer();
//        boolean ok = true;
//        ActionList actionList = null;
//        String line = "";
//
//        do
//        {
//            System.out.print(currentPlayer.getName() + " > ");
//            try {
//                line = bufferedReader.readLine();
//                actionList = interpret(line);
//            }
//            catch (GameStopRequestedException | IOException e) {
//                control.stopStage();
//                control.endGame();
//                return null;
//            }
//            catch (InvalidInputLineException e) {
//                ok = false;
//                System.out.printf("\"%s\" cannot be interpreted as a move. Try again !%n",line);
//            }
//            catch (InvalidMoveException e) {
//                ok = false;
//                System.out.println("Incorrect move. Try again !");
//            }
//        }
//        while (!ok);
//        return actionList;
//    }

    /**
     * Checks if the input line is valid
     * @param line the input line
     * @return true if the input line is valid, false otherwise
     * @throws InvalidInputLineException if the input line is invalid
     * @throws InvalidMoveException if the input line is valid but the move is invalid
     * @throws GameStopRequestedException if the input line is "stop"
     * @throws GameStopRequestedException if the input line is "stop"
     */
    public ActionList interpret(String line) throws InvalidInputLineException,InvalidMoveException, GameStopRequestedException {
        if (line.equalsIgnoreCase("stop"))
            throw new GameStopRequestedException();

        if (!inputLineIsValid(line))
            throw new InvalidInputLineException();

        ActionList actionList;

        if (line.charAt(0) == 'w' || line.charAt(0) == 'W') {
            actionList = analyseAndPlayWall(line);
        } else //if (line.charAt(0) == 'p' || line.charAt(0) == 'P')
            actionList = analyseAndPlayPawn(line);

        if(!movesVerifier.verify(actionList))
            throw new InvalidMoveException();
        return actionList;
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

    private Direction getSecondDirectionFromPawnInputLine(String inputLine) {
        return getDirectionFromPawnInputLine(inputLine.substring(1));
    }
    private ActionList analyseAndPlayPawn(String line) throws InvalidInputLineException{
        //you need to give a coordinate and a direction
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        Pawn pawn;
        if (model.getIdPlayer() == QuoridorStageModel.BLACK) {
            pawn = mod.getBlackPawn();
        } else {
            pawn = mod.getWhitePawn();
        }

        QuoridorBoard board = mod.getBoard();
        Direction direction = getDirectionFromPawnInputLine(line);
        Direction secondDirection = null;
        System.out.println(String.format(" playing pawn : direction : %s", direction));

        //TODO : verify that this is checked in the movesVerifier
        int jump = canOrNeedToJump(pawn, direction);
        if (jump == 2) {
            if (line.length() != 3) throw new InvalidInputLineException();//check the length of the input
            secondDirection = getSecondDirectionFromPawnInputLine(line);
        } else {
            if (line.length() != 2) throw new InvalidInputLineException();//check the length of the input
        }
//        boolean success = board.noWallOrEdgeInDirection(pawn,direction);
//        if (success || jump != -1) {

//        if(jump != 1) //not needed either
//        {
            int[] dest;
            if (jump!=-1) {
                if (jump==2)
                {
                    dest = pawnJump(pawn, direction, secondDirection, jump);
                }else {
                    dest = pawnJump(pawn, direction, jump);
                }
            }
            else dest = board.pawnDestination(pawn,direction);
//            if (!board.noWallOrEdgeInDirection(pawn, direction)) {
//                return false;
//            }

            //only important part
            int[] pawnCords = board.getElementCell(pawn);
            if (board.pawnExistsInDirection(pawnCords[0], pawnCords[1], direction)) {
                ;
            } else {

                dest = board.pawnDestination(pawn, direction);
            }
            ActionList actions = new ActionList(true);
            GameAction move = new MoveAction(model, pawn, "quoridorboard", dest[0], dest[1]);
            actions.addSingleAction(move);
//            ActionPlayer play = new ActionPlayer(model, this, actions);
//            play.start();
//            return true;
            return actions;
//        }
//        return false;
    }
    private BarrierMoveAction analyseBarrierMoveLine(String inputLine, Barrier barrier) {
        int x = getXFromBarrierInputLine(inputLine);
        int y = getYFromBarrierInputLine(inputLine);
        if(inputLine.toUpperCase().charAt(3)=='V') {
            x-=2;
            y--;
        }else if(inputLine.toUpperCase().charAt(3) =='H') {
            x--;
            y-=2;
        }
        BarrierType type = getBarrierTypeFromBarrierInputLine(inputLine);
        BarrierMoveAction placeWall = new BarrierMoveAction(model, barrier, "quoridorboard", x, y, type);
        return placeWall;
    }

    private BarrierType getBarrierTypeFromBarrierInputLine(String inputLine) {
        String typeString = ""+inputLine.charAt(inputLine.length()-1);
        if (typeString.equalsIgnoreCase("H")) {
            return BarrierType.HORIZONTAL;
        } else if (typeString.equalsIgnoreCase("V")) {
            return BarrierType.VERTICAL;
        }
        throw new IllegalArgumentException(inputLine+"'s last character cannot be interpreted as a BarrierType");
    }

    private int getXFromBarrierInputLine(String inputLine) {
        int x;
        int xLen = inputLine.length() - 3;

        StringBuilder xString= new StringBuilder();

        char curChar;
        for (int i = 0; i <xLen; i++) {
            curChar = inputLine.charAt(2+i);
            if(!Character.isDigit(curChar))
                return -1;
            xString.append(curChar);
        }

        x = Integer.parseInt(xString.toString()) * 2;
        return x;
    }

    private int getYFromBarrierInputLine(String inputLine) {
        int y;
        char yChar = inputLine.charAt(1);

        if(!Character.isLetter(yChar))
            return -1;

        y = letterToNumber(yChar) * 2;
        return y;
    }

    private static int letterToNumber(char letter) {
        letter = Character.toLowerCase(letter);

        return letter - 'a' + 1;
    }
    private ActionList analyseAndPlayWall(String inputLine) {
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        Barrier barrier;
        if (model.getIdPlayer() == QuoridorStageModel.BLACK) {
            barrier = mod.getBlackBarrierToPlay();
        } else {
            barrier = mod.getWhiteBarrierToPlay();
        }

        BarrierMoveAction barrierMoveAction = analyseBarrierMoveLine(inputLine, barrier);


        //TODO : alter this method so that both a player and a computer player have their move's validity evaluated by the same method

        //verified thanks to the movesVerifier

//        int row = barrierMoveAction.getRowDest();
//        int col = barrierMoveAction.getColDest();
//        BarrierType barrierType = barrierMoveAction.getBarrierType();

//        if(!board.canPlaceBarrier(row,col,barrierType))
//            return false;
//        if(!testIfThereIsStillAPathAfterAction(barrierMoveAction))
//            return false;


        ActionList actions = new ActionList(true);
        actions.addSingleAction(barrierMoveAction);

        //TODO : figure out where the testing happens
//        if (!movesVerifier.verify(actions))
//        {
//            return false;
//        }

// we don't play the actions directly anymore
//        ActionPlayer play = new ActionPlayer(model, this, actions);
//        play.start();
        return actions;
    }

    /**
     * still WIP
     * @param pawn
     * @param direction
     * @param jump
     * @return
     */
    private int[] pawnJump(Pawn pawn, Direction direction,int jump){
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


    private Direction turn(Direction direction,boolean isLeft){
        Direction[] values = Direction.values();
        int index = direction.ordinal();
        // Calculate the new index based on the boolean parameter
        int offset = isLeft ? 1 : -1;
        index = (index + offset + values.length) % values.length;
        return values[index];
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
    private int canOrNeedToJump(Pawn pawn, Direction direction){
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        QuoridorBoard board = mod.getBoard();
        int[] dest = board.pawnDestination(pawn, direction);
        if (!board.pawnExistsInDirection(pawn, direction)) {
            return -1;
        } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], direction)) {
            return 1;
        } else if ((board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, true))) && (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, false)))) {
            return 2;
        } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, true)) && !board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, false))) {
            return 3;
        } else if (board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, false)) && !board.noWallOrEdgeInDirection(dest[0], dest[1], turn(direction, true))) {
            return 4;
        } else {
            return -1;
        }


    }

    private boolean inputLineIsValid(String line) {
        // Convert the entire string to uppercase for case-insensitive comparison
        line = line.toUpperCase();
        if (line.length() == 2) {
            // The string must be two characters long
            char firstChar = line.charAt(0);
            char secondChar = line.charAt(1);
            return (firstChar == 'P' && (secondChar == 'N' || secondChar == 'S' || secondChar == 'E' || secondChar == 'W'));
        } else if (line.length() == 3) {
            // The string must be three characters long
            char firstChar = line.charAt(0);
            char secondChar = line.charAt(1);
            char thirdChar = line.charAt(2);
            return (firstChar == 'P' && (secondChar == 'N' || secondChar == 'S' || secondChar == 'E' || secondChar == 'W')&& (thirdChar == 'N' || thirdChar == 'S' || thirdChar == 'E' || thirdChar == 'W'));
        } else if (line.length() == 4) {
            // The string must be four characters long
            char firstChar = line.charAt(0);
            char secondChar = line.charAt(1);
            char thirdChar = line.charAt(2);
            char fourthChar = line.charAt(3);

            // Check the validity of each character in the string
            return (firstChar == 'W' && thirdChar >= '1' && thirdChar <= '8' &&
                    secondChar >= 'A' && secondChar <= 'H' && (fourthChar == 'H' || fourthChar == 'V'));
        } else {
            // If the length is not 2 or 4, the input is invalid
            return false;
        }
    }
}
