package quoridor.model;

import boardifier.model.action.DrawDiceAction;
import boardifier.model.action.GameAction;
import boardifier.model.action.MoveAction;
import boardifier.model.action.RemoveAction;
import quoridor.model.action.BarrierMoveAction;

/**
 * Exception thrown when a player tries to play a wrong action
 */
public class WrongActionTypeException extends Exception {

    GameAction action;

    public WrongActionTypeException(GameAction action) {
        super("Wrong action type: " + action.getClass().getName());
    }

    public WrongActionTypeException(GameAction action, String message) {
        super(message);
    }

    public GameAction getAction() {
        return action;
    }

    @Override
    public String toString() {
        if (action instanceof MoveAction) {
            return "WrongActionTypeException, you can't play this GameAction: " + ((MoveAction) action).toString();
        } else if (action instanceof BarrierMoveAction) {
            return "WrongActionTypeException, you can't play this GameAction: " + ((BarrierMoveAction) action).toString();
        } else if (action instanceof DrawDiceAction) {
            return "WrongActionTypeException, you can't play this GameAction: " + ((DrawDiceAction) action).toString();
        } else if (action instanceof RemoveAction) {
            return "WrongActionTypeException, you can't play this GameAction: " + ((RemoveAction) action).toString();
        }
        return "WrongActionTypeException, you can't play this GameAction: " + action.toString();
    }
}