package quoridor.control;

import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;

import java.io.BufferedReader;

/**
 * The HumanDecider class represents a decision-making entity controlled by a human.
 * It extends the Decider class and allows a human user to make decisions based on the provided model and controller.
 * The decisions are determined by invoking the decide() method.
 *
 * @see Decider
 */
public class HumanDecider extends Decider {

    private final BufferedReader bufferedReader;
    public HumanDecider(Model model, Controller controller, BufferedReader bufferedReader)
    {
        super(model, controller);
        this.bufferedReader = bufferedReader;
    }

    /**
     * Decides on a list of actions to perform based on the current state of the model and controller.
     * @return a list of actions to perform
     */
    @Override
    public ActionList decide() {
        return null;
    }
}
