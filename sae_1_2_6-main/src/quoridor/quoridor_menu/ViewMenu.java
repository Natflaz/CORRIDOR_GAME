package quoridor.quoridor_menu;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * Classe ViewMenu which is the view of the game's main menu.
 */
public class ViewMenu {

    private Menu app;

    private Button back;

    final Hyperlink link = new Hyperlink("https://fr.wikipedia.org/wiki/Quoridor");

    private FlowPane flow;

    private GridPane bgGrid;

    private StackPane root;

    private Button start;

    private Button pvp;

    private Button pve;

    private Button pvh;

    private Button IAvsIA;

    private Button help;

    private Button quit;

    private Button info;

    private Label text;

    private HBox container;
    public ViewMenu(Menu main){
        this.app = main;
    }

    /**
     * Method which creates the main menu.
     */
    public void createMenu() {
        text = new Label("QUORIDOR");
        text.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #000000;-fx-text-overrun: clip;");
        start = new Button("NEW GAME");
        help = new Button("HELP");
        quit = new Button("QUIT");

        dupli();
        bgGrid.add(start, 2, 2);
        start.setPrefSize(200,100);

        bgGrid.add(help, 2, 4);
        help.setPrefSize(200,100);

        bgGrid.add(quit,2,6);
        quit.setPrefSize(200,100);

    }

    /**
     * Method which creates what game to choose
     */
    public void newGamePressed(){

        text = new Label("NEW GAME ?");
        text.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-font-family: 'Arial';-fx-text-overrun: clip;");
        pvp = new Button("1 vs 1");
        pvp.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 20px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");

        pvp.setOnMouseEntered(e -> {
            pvp.setStyle("-fx-background-color: #FFB6C1; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;  -fx-text-fill: purple;-fx-font-size: 35px;");
        });

        pvp.setOnMouseExited(e -> {
            pvp.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pvp.setOnMousePressed(e -> {
            pvp.setStyle("-fx-background-color: #FFA07A; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pvp.setOnMouseReleased(e -> {
            pvp.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });
        pve = new Button("1 vs easy");
        pve.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");

        pve.setOnMouseEntered(e -> {
            pve.setStyle("-fx-background-color: #FFB6C1; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;  -fx-text-fill: purple;-fx-font-size: 35px;");
        });

        pve.setOnMouseExited(e -> {
            pve.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pve.setOnMousePressed(e -> {
            pve.setStyle("-fx-background-color: #FFA07A; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pve.setOnMouseReleased(e -> {
            pve.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });
        pvh = new Button("1 vs hard");
        pvh.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");

        pvh.setOnMouseEntered(e -> {
            pvh.setStyle("-fx-background-color: #FFB6C1; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px; -fx-text-fill: purple;-fx-font-size: 35px;");
        });

        pvh.setOnMouseExited(e -> {
            pvh.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pvh.setOnMousePressed(e -> {
            pvh.setStyle("-fx-background-color: #FFA07A; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        pvh.setOnMouseReleased(e -> {
            pvh.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        IAvsIA = new Button("IA vs IA");
        IAvsIA.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");


        IAvsIA.setOnMouseEntered(e -> {
            IAvsIA.setStyle("-fx-background-color: #FFB6C1; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px; -fx-text-fill: purple;-fx-font-size: 35px;");
        });

        IAvsIA.setOnMouseExited(e -> {
            IAvsIA.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        IAvsIA.setOnMousePressed(e -> {
            IAvsIA.setStyle("-fx-background-color: #FFA07A; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        IAvsIA.setOnMouseReleased(e -> {
            IAvsIA.setStyle("-fx-background-color: #FFC0CB; -fx-cursor: hand;-fx-border-radius: 4px;-fx-border-color:black;-fx-border-width: 2px;-fx-font-size:25px;");
        });

        dupli();

        bgGrid.add(pvp, 2, 2);
        pvp.setPrefSize(200,100);

        bgGrid.add(pve, 2, 4);
        pve.setPrefSize(200,100);
        bgGrid.add(pvh,2,6);
        pvh.setPrefSize(200,100);
        bgGrid.add(IAvsIA, 2, 8);
        IAvsIA.setPrefSize(200,100);
    }

    /**
     * This method is used to display help related to the game.
     */
    public void helpIsPressed(){
        text = new Label("How to play ;\n" +
                "\t_To move your pawn, click on it. You will see every possible destinations. Then choose one.\n" +
                "\t_To put one of your wall on the game board, click on it, choose the destination, and then scroll down or up with your mouse to put it in the direction you want.\n" +
                "Click on the following button to know every rules of the game.");

        text.setWrapText(true);
        text.setAlignment(Pos.CENTER);
        info = new Button("rules");
        text.setPrefSize(300,300);
        text.setStyle("-fx-text-alignment: justify; -fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #000000; -fx-background-color: #FFFFFF; -fx-padding: 10px; -fx-border-color: #000000; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        container = new HBox(text,info);
        container.setSpacing(20);
        info.setPadding(new Insets(10,10,10,10));
        getInfo().setOnAction(event -> app.redirectToWebsite());
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #87CEFA;");
        back = new Button("Back");
        container.getChildren().add(back);
        info.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");

        back.setStyle("-fx-background-color: #FFC0CB; " +
                "-fx-text-fill: black; " +
                "-fx-font-family: 'Arial'; " +
                "-fx-font-size: 14px; " +
                "-fx-padding: 8px 16px; " +
                "-fx-border-radius: 4px; " +
                "-fx-border-color:black;" +
                "-fx-border-width: 2px;" +
                "-fx-cursor: hand;");


        root.getChildren().add(container);
    }

    private void dupli() {
        bgGrid = new GridPane();
        bgGrid.add(text, 2,0);
        text.setAlignment(Pos.CENTER);
        text.setPrefSize(200,100);

        bgGrid.setHgap(10);
        bgGrid.setVgap(10);

        container = new HBox(bgGrid);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #87CEFA;");
        root.getChildren().add(container);
    }

    /**
     * This method is used to add listeners to the buttons.
     */
    public void addListeners() {
        ControllerMenu cb = new ControllerMenu(app, this);
        start.setOnAction((EventHandler<ActionEvent>) cb);
        quit.setOnAction((EventHandler<ActionEvent>) cb);
        help.setOnAction((EventHandler<ActionEvent>) cb);
        if (back != null)
            back.setOnAction((EventHandler<ActionEvent>)cb);
        if (pvp != null)
            pvp.setOnAction((EventHandler<ActionEvent>)cb);
        if (pve != null)
            pve.setOnAction((EventHandler<ActionEvent>)cb);
        if (pvh != null)
            pvh.setOnAction((EventHandler<ActionEvent>)cb);
        if (IAvsIA != null)
            IAvsIA.setOnAction((EventHandler<ActionEvent>)cb);
    }

    public StackPane getRoot() {
        return root;
    }

    public void setRoot(StackPane root) {
        this.root = root;
    }

    public Button getStart() {
        return start;
    }

    public void setStart(Button start) {
        this.start = start;
    }

    public Button getPvp() {
        return pvp;
    }

    public Button getPve() {
        return pve;
    }

    public Button getPvh() {
        return pvh;
    }

    public Button getHelp() {
        return help;
    }

    public Button getQuit() {
        return quit;
    }

    public Button getInfo() {
        return info;
    }

    public Button getBack() {
        return back;
    }

    public void setBack(Button back) {
        this.back = back;
    }

    public Button getIAvsIA() {
        return IAvsIA;
    }
}
