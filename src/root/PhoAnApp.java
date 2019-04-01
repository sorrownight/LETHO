package root;

import com.InteractivePanes.Client;
import com.InteractivePanes.Pickup;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public final class PhoAnApp extends Application
{
    private static Scene main;
    private static Client clientPane;
    private static Pickup pickup;


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        clientPane = new Client();
        pickup = new Pickup();

        clientPane.setBackground(new Background(
                new BackgroundImage(new Image("background.jpg"), BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(1200,900,true,true,
                        true,true))));

        pickup.setBackground(new Background(
                new BackgroundImage(new Image("background.jpg"), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(1200,900,true,true,
                                true,true))));

        main = new Scene(clientPane,1200,900);

        primaryStage.setTitle("Pho An App");
        primaryStage.setScene(main);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void swapRoot(BorderPane current)
    {
        if (current instanceof Client)
            main.setRoot(pickup);
        else
            main.setRoot(clientPane);

    }
}
