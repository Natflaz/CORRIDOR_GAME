package quoridor.control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.ControllerMouse;
import boardifier.model.Coord2D;
import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.action.MoveAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import quoridor.model.*;
import quoridor.model.action.BarrierMoveAction;

import java.util.List;

/**
 * The QuoridorControllerMouse class represents a controller mouse specific to the game Quoridor.
 * It extends the ControllerMouse class and implements the EventHandler interface for handling MouseEvent.
 *
 * @see ControllerMouse
 * @see EventHandler
 */
public class QuoridorControllerMouse extends ControllerMouse implements EventHandler<MouseEvent> {
    public QuoridorControllerMouse(Model model, View view, Controller control) {
        super(model, view, control);
        view.getRootPane().addEventFilter(ScrollEvent.SCROLL, this::handle);
    }

    /**
     * Scroll event handler (so when user scroll the mouse)
     * @param event any scroll event
     */
    public void handle(ScrollEvent event) {
        if (!model.isCaptureMouseEvent()) return;
        if (model.getSelected().size() < 1) return;
        GameElement element = model.getSelected().get(0);
        if (element.getGrid() == ((QuoridorStageModel) model.getGameStage()).getBoard()) {
            if (element instanceof Barrier) {
                Barrier barrier = (Barrier) element;
                int[] pos = barrier.getGrid().getElementCell(barrier);
                List<GameElement> elements = ((QuoridorStageModel) model.getGameStage()).getBoard().getElements(pos[0], pos[1]);
                Barrier previous = null;
                for (GameElement e : elements) {
                    if (e instanceof Barrier) {
                        previous = (Barrier) e;
                    }
                }
                if (event.getDeltaY() > 0 || event.getDeltaY() < 0) {
                    if (previous != null) {
                        if (previous.getBarrierType() == BarrierType.PREVIEW_VERTICAL)
                            previous.setBarrierType(BarrierType.PREVIEW_HORIZONTAL);
                        else
                            previous.setBarrierType(BarrierType.PREVIEW_VERTICAL);
                        return;
                    }
                    if (barrier.getBarrierType() == BarrierType.PREVIEW_VERTICAL)
                        barrier.setBarrierType(BarrierType.PREVIEW_HORIZONTAL);
                    else
                        barrier.setBarrierType(BarrierType.PREVIEW_VERTICAL);
                }
            }
        }
    }

    /**
     * Mouse movement handler
     * It will manage every click, will check the element and play it if it is a valid move
     * @param event any mouse event
     */
    public void handle(MouseEvent event) {
        if (!model.isCaptureMouseEvent()) return;

        Coord2D click = new Coord2D(event.getSceneX(), event.getSceneY());
        List<GameElement> list = control.elementsAt(click);

        QuoridorStageModel stageModel = (QuoridorStageModel) model.getGameStage();

        if (stageModel.getState() != QuoridorStageModel.STATE_SELECT) {
            for (GameElement element : list) {
                if (!(element == stageModel.getBoard())) {
                    if (!element.isSelected()) {
                        if (stageModel.getState() != QuoridorStageModel.STATE_BARRIER) {
                            model.unselectAll();
                        }
                        stageModel.setState(QuoridorStageModel.STATE_SELECT);
                        stageModel.getBoard().resetReachableCells(false);
                        if (element instanceof Barrier) {
                            Barrier barrier = (Barrier) element;
                            if (stageModel.getSelected().size() > 0) {
                                GameElement e = stageModel.getSelected().get(0);
                                if (e != null && barrier.getColor() == model.getIdPlayer() && (stageModel.getState() != QuoridorStageModel.STATE_DEST || !(e instanceof Pawn))) {
                                    if (barrier.getGrid() == stageModel.getBoard()) {
                                        element.unselect();
                                        continue;
                                    }
                                    element.toggleSelected();
                                    stageModel.setState(QuoridorStageModel.STATE_BARRIER);
                                    break;
                                }
                            } else {
                                if (barrier.getColor() == model.getIdPlayer() && stageModel.getState() != QuoridorStageModel.STATE_DEST) {
                                    if (barrier.getGrid() == stageModel.getBoard()) {
                                        element.unselect();
                                        continue;
                                    }
                                    element.toggleSelected();
                                    stageModel.setState(QuoridorStageModel.STATE_BARRIER);
                                    break;
                                }
                            }
                        } else if (element instanceof Pawn) {
                            Pawn pawn = (Pawn) element;
                            if (stageModel.getSelected().size() > 0) {
                                GameElement e = stageModel.getSelected().get(0);
                                if (e!= null && pawn.getColor() == model.getIdPlayer() && (stageModel.getState() != QuoridorStageModel.STATE_DEST && (e instanceof Barrier))) {
                                    e.unselect();
                                    element.toggleSelected();
                                    stageModel.setState(QuoridorStageModel.STATE_DEST);
                                    break;
                                }
                            } else {
                                if (pawn.getColor() == model.getIdPlayer()) {
                                    element.toggleSelected();
                                    stageModel.setState(QuoridorStageModel.STATE_DEST);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        boolean isBarrier = false;
        BarrierType newType = null;
        int state = stageModel.getState();
        int[] dest = null;

        if (stageModel.getState() == QuoridorStageModel.STATE_SELECT) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("pawn")) {
                    Pawn pawn = (Pawn) element;
                    if (pawn.getColor() == model.getIdPlayer()) {
                        element.toggleSelected();
                        stageModel.setState(QuoridorStageModel.STATE_DEST);
                        return;
                    }
                } else if (element.getType() == ElementTypes.getType("barrier")) {
                    Barrier barrier = (Barrier) element;
                    if (barrier.getGrid() == stageModel.getBoard()) {
                        element.unselect();
                        continue;
                    }
                    if (barrier.getColor() == model.getIdPlayer()) {
                        element.toggleSelected();
                        stageModel.setState(QuoridorStageModel.STATE_BARRIER);
                        return;
                    }
                }
            }
            return;
        } else if (stageModel.getState() == QuoridorStageModel.STATE_BARRIER) {
            boolean clickedOutsideOfBoard = true;
            for (GameElement element : list) {
                if (element == stageModel.getBoard()) {
                    clickedOutsideOfBoard = false;
                }
            }
            if (clickedOutsideOfBoard) {
                return;
            }
            if (model.getSelected().size() < 1) return;
            GameElement element = model.getSelected().get(0);
            if (element instanceof Barrier) {
                Barrier barrier = (Barrier) element;
                if (barrier.getColor() == model.getIdPlayer()) {
                    newType = BarrierType.PREVIEW_HORIZONTAL;
                    isBarrier = true;
                }
            }
            stageModel.setState(QuoridorStageModel.STATE_DEST);
        } else if (stageModel.getState() == QuoridorStageModel.STATE_DEST) {
            for (GameElement element : list) {
                if (element instanceof Barrier) {
                    if (element.getGrid() == stageModel.getBoard()) {
                        element.unselect();
                        continue;
                    }
                    if (((Barrier) element).getColor() == model.getIdPlayer()) {
                        if (!element.isSelected()) {
                            element.toggleSelected();
                            break;
                        }
                    }
                }
            }
            if (model.getSelected().size() < 1) return;

            GameElement element = model.getSelected().get(0);
            if (element instanceof Barrier) {
                Barrier barrier = (Barrier) element;
                newType = BarrierType.HORIZONTAL;
                if (barrier.getBarrierType() == BarrierType.PREVIEW_VERTICAL) {
                    newType = BarrierType.VERTICAL;
                }
                dest = element.getGrid().getElementCell(element);
            }
        } else {
            return;
        }
        boolean boardClicked = false;
        for (GameElement element : list) {
            if (element == stageModel.getBoard()) {
                boardClicked = true;
                break;
            }
        }
        if (!boardClicked) return;
        if (model.getSelected().size() < 1) return;
        GameElement element = model.getSelected().get(0);
        QuoridorBoard board = stageModel.getBoard();
        GridLook lookBoard = (GridLook) control.getElementLook(board);
        if (dest == null) {
            dest = lookBoard.getCellFromSceneLocation(click);
        }
        ActionList action;
        if (isBarrier) {
            action = new ActionList(false);
        } else {
            action = new ActionList(true);
        }
        Coord2D center = lookBoard.getRootPaneLocationForCellCenter(dest[0], dest[1]);
        MoveAction move;
        if (element instanceof Barrier) {
            if (((Barrier) element).getBarrierType() == BarrierType.PREVIEW_HORIZONTAL) {
                newType = BarrierType.HORIZONTAL;
            } else if (((Barrier) element).getBarrierType() == BarrierType.PREVIEW_VERTICAL) {
                newType = BarrierType.VERTICAL;
            }
            move = new BarrierMoveAction(model, (Barrier) element, "quoridorboard", dest[0], dest[1], newType, AnimationTypes.NONE, center.getX(), center.getY(), 10);
        } else {
            move = new MoveAction(model, element, "quoridorboard", dest[0], dest[1], AnimationTypes.NONE, center.getX(), center.getY(), 10);
        }
        action.addSingleAction(move);

        MovesVerifier movesVerifier = new MovesVerifier(model, control);
        System.out.println(movesVerifier.verify(action));
        if (movesVerifier.verify(action) || (state == QuoridorStageModel.STATE_DEST && element instanceof Barrier)) {
            if (state != QuoridorStageModel.STATE_BARRIER) {
                stageModel.unselectAll();
                stageModel.setState(QuoridorStageModel.STATE_SELECT);
            }
            if (element instanceof Barrier) {
                ((Barrier) element).setBarrierType(newType);
            }
            ActionPlayer play = new ActionPlayer(model, control, action);
            play.start();
        } else {
            if (element instanceof Barrier) {
                stageModel.setState(QuoridorStageModel.STATE_BARRIER);
            }
        }
    }
}
