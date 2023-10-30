package quoridor.view;

import boardifier.model.GameStageModel;
import boardifier.view.GameStageView;
import boardifier.view.TextLook;
import quoridor.model.QuoridorStageModel;

/**
 * This class is used to display the stage of the Quoridor game.
 * @see GameStageView
 */
public class QuoridorStageView extends GameStageView {

    public QuoridorStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
        width = 1000;
        height = 750;
    }

    /**
     * Method createLooks which is called when the stage is created.
     */
    @Override
    public void createLooks() {
        QuoridorStageModel model = (QuoridorStageModel) gameStageModel;

        addLook(new QuoridorGridLook(700, model.getBoard()));

        addLook(new BarrierPotLook(69, 69, model.getBlackBarrierPot(), model.getModel(), model.getModel().getPlayers().get(0).getName()));
        addLook(new BarrierPotLook(69, 69, model.getWhiteBarrierPot(), model.getModel(), model.getModel().getPlayers().get(0).getName()));

        addLook(new PawnLook(model.getWhitePawn()));
        addLook(new PawnLook(model.getBlackPawn()));

        for (int i = 0 ; i < 10 ; i++) {
            addLook(new BarrierLook(model.getBlackBarrierArray()[i]));
            addLook(new BarrierLook(model.getWhiteBarrierArray()[i]));
        }

        addLook(new TextLook(24, "0x000000", model.getPlayerName()));
    }
}
