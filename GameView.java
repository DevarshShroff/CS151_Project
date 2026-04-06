import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// builds the window and handles all button/menu events
public class GameView {

    private static final int DEFAULT_ROUNDS = 20;

    private final Stage stage;
    private GUIGameController controller;
    private int totalRounds = DEFAULT_ROUNDS;

    // labels that get updated after each round
    private Label roundLabel;
    private Label humanChoiceLabel;
    private Label predictionLabel;
    private Label computerChoiceLabel;
    private Label winnerLabel;
    private Label humanWinsLabel;
    private Label computerWinsLabel;
    private Label tiesLabel;
    private Label statusLabel;
    private Button rockBtn, paperBtn, scissorsBtn;

    private BorderPane root;

    public GameView(Stage stage) {
        this.stage = stage;
        buildUI();
        startNewGame(DEFAULT_ROUNDS);
    }

    public BorderPane getRoot() {
        return root;
    }

    // saves ML data when the user closes the window
    public void onClose() {
        if (controller != null) {
            controller.saveData();
        }
    }

    private void buildUI() {
        root = new BorderPane();
        root.setTop(buildMenuBar());
        root.setCenter(buildGamePanel());
        root.setBottom(buildStatusBar());
    }

    private MenuBar buildMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu gameMenu = new Menu("Game");
        MenuItem startNewGameItem = new MenuItem("Start a new game");
        startNewGameItem.setOnAction(e -> handleNewGame());
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> {
            if (controller != null) controller.saveData();
            Platform.exit();
        });
        gameMenu.getItems().addAll(startNewGameItem, new SeparatorMenuItem(), exitItem);

        Menu helpMenu = new Menu("Help");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> handleAbout());
        helpMenu.getItems().add(aboutItem);

        menuBar.getMenus().addAll(gameMenu, helpMenu);
        return menuBar;
    }

    private VBox buildGamePanel() {
        VBox panel = new VBox(8);
        panel.setPadding(new Insets(16, 24, 16, 24));
        panel.setAlignment(Pos.CENTER);

        roundLabel = new Label("Round: 1");
        roundLabel.setFont(Font.font("System", FontWeight.BOLD, 18));

        Label humanHeader = boldLabel("Human", 14);

        rockBtn     = new Button("Rock");
        paperBtn    = new Button("Paper");
        scissorsBtn = new Button("Scissors");

        // each button plays a round with that move
        rockBtn.setOnAction(e     -> handleHumanChoice(Move.ROCK));
        paperBtn.setOnAction(e    -> handleHumanChoice(Move.PAPER));
        scissorsBtn.setOnAction(e -> handleHumanChoice(Move.SCISSORS));

        HBox buttonRow = centeredHBox(5, new Label("Choose:"), rockBtn, paperBtn, scissorsBtn);

        humanChoiceLabel = new Label("\u2014");
        humanChoiceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        HBox humanChoiceRow = centeredHBox(6, new Label("Human chooses:"), humanChoiceLabel);

        Label computerHeader = boldLabel("Computer", 14);

        predictionLabel = new Label("\u2014");
        HBox predictionRow = centeredHBox(6, new Label("Predicted human choice:"), predictionLabel);

        computerChoiceLabel = new Label("\u2014");
        computerChoiceLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        HBox computerChoiceRow = centeredHBox(6, new Label("Therefore computer chooses:"), computerChoiceLabel);

        Label winnerHeaderLabel = boldLabel("The winner:", 14);
        winnerLabel = new Label("\u2014");
        HBox winnerRow = centeredHBox(6, winnerHeaderLabel, winnerLabel);

        Label statsHeader = boldLabel("Statistics", 14);

        humanWinsLabel    = new Label("0");
        computerWinsLabel = new Label("0");
        tiesLabel         = new Label("0");

        HBox humanWinsRow    = statRow("Human wins:",    humanWinsLabel);
        HBox computerWinsRow = statRow("Computer wins:", computerWinsLabel);
        HBox tiesRow         = statRow("Ties:",          tiesLabel);

        panel.getChildren().addAll(
                roundLabel, new Separator(),
                humanHeader, buttonRow, humanChoiceRow, new Separator(),
                computerHeader, predictionRow, computerChoiceRow, new Separator(),
                winnerRow, new Separator(),
                statsHeader, humanWinsRow, computerWinsRow, tiesRow
        );

        return panel;
    }

    private HBox buildStatusBar() {
        statusLabel = new Label("Welcome to the Rock Paper Scissors game!");
        statusLabel.setPadding(new Insets(4, 8, 4, 8));
        HBox bar = new HBox(statusLabel);
        bar.setStyle("-fx-border-color: #aaa; -fx-border-width: 1 0 0 0; -fx-background-color: #f0f0f0;");
        return bar;
    }

    // runs when the human picks Rock, Paper, or Scissors
    private void handleHumanChoice(Move move) {
        if (controller == null || controller.isGameOver()) return;

        RoundResult result = controller.playRound(move);
        updateDisplay(result);

        if (controller.isGameOver()) {
            setButtonsDisabled(true);
            controller.saveData();
            statusLabel.setText("Game over! Use Game \u2192 Start a new game to play again.");
            stage.setTitle("Rock Paper Scissors: " + totalRounds + " rounds/game  [Game Over]");
        }
    }

    private void handleNewGame() {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(totalRounds));
        dialog.setTitle("Start a New Game");
        dialog.setHeaderText("Start a New Game");
        dialog.setContentText("Number of rounds per game:");
        dialog.initOwner(stage);

        // pressing cancel does nothing
        dialog.showAndWait().ifPresent(input -> {
            String trimmed = input.trim();
            if (trimmed.isEmpty()) {
                startNewGame(totalRounds);
                return;
            }
            try {
                int rounds = Integer.parseInt(trimmed);
                if (rounds > 0) {
                    totalRounds = rounds;
                    startNewGame(totalRounds);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Invalid Input",
                            "Please enter a positive number.\n" +
                                    "Starting with " + totalRounds + " rounds.");
                    startNewGame(totalRounds);
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Invalid Input",
                        "\"" + trimmed + "\" is not a valid number.\n" +
                                "Starting with " + totalRounds + " rounds.");
                startNewGame(totalRounds);
            }
        });
    }

    private void handleAbout() {
        showAlert(Alert.AlertType.INFORMATION, "About Rock Paper Scissors",
                "CS 151 — Object-Oriented Analysis and Design\n" +
                        "Assignment #5: GUI-Based RPS Game\n\n" +
                        "The computer uses machine learning to predict\n" +
                        "your moves and counter them.\n\n" +
                        "Frequency data is saved to ml_frequency_data.txt.");
    }

    // updates every label after a round finishes
    private void updateDisplay(RoundResult result) {
        roundLabel.setText("Round: " + result.round);
        humanChoiceLabel.setText(titleCase(result.humanMove.toString()));

        if (result.prediction != null) {
            predictionLabel.setText(titleCase(result.prediction.toString()));
        } else {
            predictionLabel.setText("Not enough data yet");
        }

        computerChoiceLabel.setText(titleCase(result.computerMove.toString()));
        winnerLabel.setText(result.getWinnerText());
        humanWinsLabel.setText(String.valueOf(result.humanWins));
        computerWinsLabel.setText(String.valueOf(result.computerWins));
        tiesLabel.setText(String.valueOf(result.draws));

        switch (result.result) {
            case 0:  statusLabel.setText("Round " + result.round + ": It's a tie!"); break;
            case 1:  statusLabel.setText("Round " + result.round + ": Human wins!"); break;
            default: statusLabel.setText("Round " + result.round + ": Computer wins!"); break;
        }
    }

    private void startNewGame(int rounds) {
        Player human    = new HumanGUIPlayer("Human");
        Player computer = ComputerPlayerFactory.createComputerPlayer(Algorithm.MACHINE_LEARNING);
        IRules rules    = new ClassicRules();
        controller      = new GUIGameController(human, computer, rules, rounds);

        roundLabel.setText("Round: 1");
        humanChoiceLabel.setText("\u2014");
        predictionLabel.setText("\u2014");
        computerChoiceLabel.setText("\u2014");
        winnerLabel.setText("\u2014");
        humanWinsLabel.setText("0");
        computerWinsLabel.setText("0");
        tiesLabel.setText("0");
        statusLabel.setText("Welcome to the Rock Paper Scissors game!");
        setButtonsDisabled(false);
        stage.setTitle("Rock Paper Scissors: " + rounds + " rounds/game");
    }

    private void setButtonsDisabled(boolean disabled) {
        rockBtn.setDisable(disabled);
        paperBtn.setDisable(disabled);
        scissorsBtn.setDisable(disabled);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.initOwner(stage);
        alert.showAndWait();
    }

    // converts "ROCK" to "Rock"
    private String titleCase(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
    }

    private Label boldLabel(String text, double size) {
        Label l = new Label(text);
        l.setFont(Font.font("System", FontWeight.BOLD, size));
        return l;
    }

    private HBox centeredHBox(double spacing, javafx.scene.Node... nodes) {
        HBox box = new HBox(spacing, nodes);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private HBox statRow(String labelText, Label valueLabel) {
        HBox row = new HBox(5, new Label(labelText), valueLabel);
        row.setAlignment(Pos.CENTER);
        return row;
    }
}