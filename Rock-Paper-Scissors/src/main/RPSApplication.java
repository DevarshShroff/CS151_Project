import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

// entry point for the JavaFX app
public class RPSApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameView view = new GameView(primaryStage);
        primaryStage.setScene(new Scene(view.getRoot(), 440, 500));
        primaryStage.setTitle("Rock Paper Scissors: 20 rounds/game");
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(e -> view.onClose());
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}