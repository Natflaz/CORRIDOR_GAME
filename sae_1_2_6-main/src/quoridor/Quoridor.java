package quoridor;

import boardifier.control.StageFactory;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.application.Application;
import javafx.stage.Stage;
import quoridor.control.QuoridorController;
import quoridor.quoridor_menu.Menu;
import quoridor.view.QuoridorRootPane;
import quoridor.view.QuoridorView;

/**
 * This class is used to launch the Quoridor game.
 */
public class Quoridor extends Application {

    private static int mode;

    private Model model;
    private View view;
    private QuoridorController control;
    private Stage primaryStage;

    public Quoridor() {}

    private int getPlayType(String arg) throws IllegalArgumentException {
        int playType = Integer.parseInt(arg);
        if(playType < 0 || playType>2)
            throw  new IllegalArgumentException();
        return playType;
    }

    private void addPlayer(int playType, String suffix) {
        if (playType == QuoridorController.GRAPHIC) {
            model.addHumanPlayer("player"+suffix);
        }
        else if (playType == QuoridorController.COINFLIPPINGAI) {
            model.addComputerPlayer("computer_coinFlipAI"+suffix);
        }
        else if (playType == QuoridorController.MINIMAX)
            model.addComputerPlayer("computer_minimaxAI"+suffix);
    }

    private void setup(int aiTypePWhite, int aiTypePBlack) {
        addPlayer(aiTypePBlack,"1");
        addPlayer(aiTypePWhite,"2");

        StageFactory.registerModelAndView("quoridor", "quoridor.model.QuoridorStageModel", "quoridor.view.QuoridorStageView");

        control.setAiTypePBlack(aiTypePBlack);
        control.setAiTypePWhite(aiTypePWhite);
    }

    private void execute(Stage stage) {
        try {
            stage.show();
            control.startGame();
        }
        catch(GameException e) {
            System.out.println("Cannot start the game. Abort");
        }
    }

    /**
     * This method is used to launch the Quoridor game.
     * @param stage the stage of the game
     */
    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        Menu menu = new Menu();
        menu.setQuoridor(this);
        Stage menuStage = new Stage();
        menu.start(menuStage);
        menuStage.show();
    }

    /**
     * This method is used to start the game.
     * @param aiTypePWhite the type of the AI for the white player
     * @param aiTypePBlack the type of the AI for the black player
     */
    public void startGame(int aiTypePWhite, int aiTypePBlack) {
        model = new Model();

        StageFactory.registerModelAndView("quoridor", "quoridor.model.QuoridorStageModel", "quoridor.view.QuoridorStageView");
        QuoridorRootPane rootPane = new QuoridorRootPane();
        QuoridorView view = new QuoridorView(model, primaryStage, rootPane);

        QuoridorController controller = new QuoridorController(model, view, aiTypePWhite, aiTypePBlack);
        control = controller;

        setup(aiTypePWhite, aiTypePBlack);

        controller.setFirstStageName("quoridor");
        primaryStage.setTitle("Quoridor");
        primaryStage.setHeight(750);
        primaryStage.setWidth(1000);
        primaryStage.show();
        TimeUtil.delay(100, () -> {
            try {
                controller.startGame();
            } catch (GameException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                mode = Integer.parseInt(args[0]);
                if ((mode <0) || (mode>2)) mode = 0;
            }
            catch(NumberFormatException e) {
                mode = 0;
            }
        } else {
            mode = 0;
        }
        launch(args);
    }
}
