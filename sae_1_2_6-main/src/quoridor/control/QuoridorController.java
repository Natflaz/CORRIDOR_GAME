package quoridor.control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import quoridor.TimeUtil;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

/**
 * The QuoridorController class represents a controller for the game Quoridor.
 * It extends the Controller class and provides the logic and functionality for managing the game flow and interactions.
 *
 * @see Controller
 */
public class QuoridorController extends Controller {

    public final static int GRAPHIC =0,COINFLIPPINGAI=1,MINIMAX=2;
    boolean firstPlayer;
    private BufferedReader consoleIn;
    private Graph graph;
    private int aiTypePWhite;
    private int aiTypePBlack;

    private CoinFlipDecider coinFlipDecider = new CoinFlipDecider(model, this,new CoinFlipper(), new Graph());
    private MinimaxDecider minimaxDecider = new MinimaxDecider(model, this);
    private boolean stopUpdatingView = false;

    public QuoridorController(Model model, View view, int aiTypePWhite, int aiTypePBlack) {
        super(model, view);
        firstPlayer = true;
        this.aiTypePWhite = aiTypePWhite;
        this.aiTypePBlack = aiTypePBlack;
        setControlKey(new QuoridorControllerKey(model, view, this));
        setControlMouse(new QuoridorControllerMouse(model, view, this));
        setControlAction(new QuoridorControllerAction(model, view, this));
    }
    /**
     * creates a QuoridorController so that the game may be played.
     * @param model
     * @param view
     * @param consoleIn
     * @param aiTypePWhite 0 :
     * @param aiTypePBlack 0 : player input, 1 : coinFlipper, 2 :
     * @param graph the graph is used to check if a path blocks a pawn
     */
    public QuoridorController(Model model, View view, BufferedReader consoleIn, int aiTypePWhite, int aiTypePBlack,Graph graph) {
        super(model, view);
        this.consoleIn = consoleIn;
        this.aiTypePWhite = aiTypePWhite;
        this.aiTypePBlack = aiTypePBlack;
        this.graph = graph;
        firstPlayer = true;
        setControlKey(new QuoridorControllerKey(model, view, this));
        setControlMouse(new QuoridorControllerMouse(model, view, this));
        setControlAction(new QuoridorControllerAction(model, view, this));
    }
    /**
     * this constructor cannot be called under most circumstances, as the proper way to instantiate a Decider
     * (ie {@link #minimaxDecider} or {@link #coinFlipDecider}) requires a reference to the controller itself. This constructor
     * is used for testing so that mocks can be passed as the deciders.
     */
    QuoridorController(Model model, View view, BufferedReader consoleIn, int aiTypePWhite, int aiTypePBlack, Graph graph, MinimaxDecider minimaxAI, CoinFlipDecider coinFlipAI) {
        super(model, view);
        this.consoleIn = consoleIn;
        this.aiTypePWhite = aiTypePWhite;
        this.aiTypePBlack = aiTypePBlack;
        this.graph = graph;
        firstPlayer = true;

        this.minimaxDecider = minimaxAI;
        this.coinFlipDecider = coinFlipAI;
    }

    /**
     * makes the next player play
     */
    public void nextPlayer() {
        if (firstPlayer) {
            firstPlayer = false;
        }
        model.setNextPlayer();
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            TimeUtil.delay(1000, this::computerPlays);
        }
        QuoridorStageModel stageModel = ((QuoridorStageModel) model.getGameStage());
        stageModel.getPlayerName().setText(model.getCurrentPlayer().getName());
        computeEndOfGame();
        if (model.isEndStage()) {
            endGame();
        }
    }

    /**
     * Updates all the elements of the game
     */
    @Override
    public void update() {
        if (inUpdate) {
            System.err.println("Abnormal case: concurrent updates");
        }
        inUpdate = true;

        // update the model of all elements :
        mapElementLook.forEach((k,v) -> {
            // get the bounds of the look
            Bounds b = v.getGroup().getBoundsInParent();
            // get the geometry of the grid that owns the element, if it exists
            if (k.getGrid() != null) {
                GridLook look = getElementGridLook(k);
                k.update(b.getWidth(), b.getHeight(), look.getGeometry());
            }
            else {
                k.update(b.getWidth(), b.getHeight(), null);
            }
            // if the element must be auto-localized in its cell center
            if (k.isAutoLocChanged()) {
                setElementLocationToCellCenter(k);
            }
        });
        // update the looks
        if (!stopUpdatingView)
            view.update();
        // reset changed indicators
        mapElementLook.forEach((k,v) -> {
            k.resetChanged();
        });

        if (model.isEndStage()) {
            controlAnimation.stopAnimation();
            Platform.runLater( () -> {
                stopStage();});
        }
        else if (model.isEndGame()) {
            controlAnimation.stopAnimation();
            Platform.runLater( () -> {endGame();});
        }

        inUpdate = false;
    }

    /**
     * checks if the view will be updated or not
     * @return
     */
    public boolean isStopUpdatingView() {
        return stopUpdatingView;
    }

    /**
     * sets if the view will be updated or not
     * @param stopUpdatingView
     */
    public void setStopUpdatingView(boolean stopUpdatingView) {
        this.stopUpdatingView = stopUpdatingView;
    }

    private void humanPlays(Player p) {
        boolean ok = false;
        while (!ok) {
            try {
                ok = true;
                if (!ok) {
                    System.out.println("Incorrect instruction. Retry !");
                    throw new IOException();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void computerPlays() {
        System.out.println("COMPUTER PLAYS");

        Decider decider;

        int aiType;
        if(model.getIdPlayer() == QuoridorStageModel.BLACK)
            aiType = aiTypePBlack;
        else
            aiType = aiTypePWhite;

        switch (aiType) {
            case COINFLIPPINGAI :
                System.out.println("playing with coinflipAI");
                decider = coinFlipDecider;
                break;
            case MINIMAX :
                System.out.println("playing with minimaxAI");
                decider = minimaxDecider;
                break;
            case GRAPHIC:
                throw new IllegalArgumentException("an AI is supposed to play, but the aiType was set to 0");
            default:
                throw new IllegalArgumentException("the Controller is not set up to handle an AI type of : "+aiType);
        }

        ActionPlayer play = new ActionPlayer(model, this, decider, null);
        play.start();
    }

    private void computeEndOfGame() {
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        Pawn whitePawn = mod.getWhitePawn();
        Pawn blackPawn = mod.getBlackPawn();
        if (mod.getBoard().isOnEdge(whitePawn, Direction.SOUTH)){
            model.setIdWinner(1);
            model.stopStage();
        }
        if(mod.getBoard().isOnEdge(blackPawn, Direction.NORTH)){
            model.setIdWinner(0);
            model.stopStage();
        }
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

    private boolean analyseAndPlay(String line) {
//        System.out.println("entré analyse&P");
        boolean ok = false;
        if (!inputLineIsValid(line)) {
            return false;
        }
        if (line.charAt(0) == 'w' || line.charAt(0) == 'W') {
            ok = analyseAndPlayWall(line);
        } else if (line.charAt(0) == 'p' || line.charAt(0) == 'P') {
            ok = analyseAndPlayPawn(line);
        }
        return ok;
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
    private boolean analyseAndPlayWall(String inputLine) {
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

        int row = barrierMoveAction.getRowDest();
        int col = barrierMoveAction.getColDest();
        BarrierType barrierType = barrierMoveAction.getBarrierType();
        if(!board.canPlaceBarrier(row,col,barrierType))
            return false;
        if(!testIfThereIsStillAPathAfterAction(barrierMoveAction))
            return false;


        ActionList actions = new ActionList(true);
        actions.addSingleAction(barrierMoveAction);
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }

    /**
     * this method look if the inputted String is valid or not
     * @param line the input line
     * @return whether it was valid
     */
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
    private boolean analyseAndPlayPawn(String line) {
        //you need to give a coordinate and a direction
        QuoridorStageModel mod = (QuoridorStageModel) model.getGameStage();
        Pawn pawn;
        if (model.getIdPlayer() == 0) {
            pawn = mod.getBlackPawn();
        } else {
            pawn = mod.getWhitePawn();
        }

        QuoridorBoard board = mod.getBoard();
        Direction direction = getDirectionFromPawnInputLine(line);
        Direction secondDirection = null;
        System.out.println(String.format(" playing pawn : direction : %s", direction));


        int jump = canOrNeedToJump(pawn, direction);
        if (jump == 2) {
            if (line.length() != 3) return false;//check the length of the input
            secondDirection = getSecondDirectionFromPawnInputLine(line);
        } else {
            if (line.length() != 2) return false;//check the length of the input
        }
        boolean success = board.noWallOrEdgeInDirection(pawn,direction);
        if (success || jump != -1) {
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
            if (!board.noWallOrEdgeInDirection(pawn, direction)) {
                return false;
            }

            int[] pawnCords = board.getElementCell(pawn);
            if (board.pawnExistsInDirection(pawnCords[0], pawnCords[1], direction)) {
                ;
            } else {

                dest = board.pawnDestination(pawn, direction);
            }


            ActionList actions = new ActionList(true);
            GameAction move = new MoveAction(model, pawn, "quoridorboard", dest[0], dest[1]);
            actions.addSingleAction(move);
            ActionPlayer play = new ActionPlayer(model, this, actions);
            play.start();
            return true;
        }
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
     * Check if there is still a path to win after the action
     * @param BarrierAction the action that is going to be executed
     * @return true if there is still a path to win after the action
     */
    public boolean testIfThereIsStillAPathAfterAction(BarrierMoveAction BarrierAction){
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

    public Model getModel() {
        return model;
    }

    public View getView() {
        return view;
    }

    public void setAiTypePWhite(int aiTypePWhite) {
        this.aiTypePWhite = aiTypePWhite;
    }

    public void setAiTypePBlack(int aiTypePBlack) {
        this.aiTypePBlack = aiTypePBlack;
    }
}