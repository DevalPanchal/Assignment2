package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends Application {
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        Main.primaryStage = stage;
    }

    public static Stage getPrimaryStage () {
        return Main.primaryStage;
    }

    public static String fileDestination;
    public static String computerName;

    public static void setFileDestination(String file) {
        fileDestination = file;
    }

    public static String getFileDestination() {
        return fileDestination;
    }

    public static void setComputerName(String name) {
        computerName = name;
    }

    public static String getComputerName() {
        return computerName;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setPrimaryStage(primaryStage);
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("File Sharing");
        primaryStage.setScene(new Scene(root, 515, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        //System.out.println("This is the first args " + args[0]);
//        fileDestination = args[0];
        setFileDestination(args[0]);

        setComputerName(args[1]);
        System.out.println(args[0] + " " + args[1]);
//        System.out.println(fileDestination);
        launch(args);
    }
}
