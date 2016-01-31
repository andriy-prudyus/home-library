package application;

import database.DatabaseStructure;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import log.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

    private static final Logger logger = Logger.getLogger(Log.class.getName());

    static {
        Log.prepareLogConfiguration();
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            new DatabaseStructure().initialize();

            Parent mainPane = FXMLLoader.load(getClass().getResource("/view/template/home/Home.fxml"));

            primaryStage.setScene(new Scene(mainPane));
            primaryStage.setTitle("Home Library");
            primaryStage.setMaximized(true);
            primaryStage.show();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error starting stage", e);
        }
    }

    public static void main(String[] args) {
        logger.info("Starting HomeLibrary...");

        launch(args);

        logger.info("Finishing HomeLibrary...");
    }

}
