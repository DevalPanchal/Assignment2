package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {
    private final int PORT = 8080;
    private final String HOSTNAME = "localhost";
    private final Socket socket = new Socket(HOSTNAME, PORT);

    @FXML private ListView<String> clientFiles;
    @FXML private ListView<String> serverFiles;

    @FXML private final File clientDir = new File("ClientDownload/");
    @FXML private final File serverDir = new File("ServerDownload/");

    @FXML private Button UploadButton;

    public Controller() throws IOException { /* */ }

    @FXML
    MenuItem exitClient;

    public void initialize() throws IOException {
        clientFiles.setItems(FXCollections.observableArrayList(clientDir.list()));
        serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));

        exitClient.setOnAction(actionEvent -> exit());

        UploadButton.setOnAction(actionEvent -> {
            try {
                upload("test.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void upload(String file) throws IOException {
        var output = new DataOutputStream(socket.getOutputStream());
        var input = new FileInputStream(file);

        byte[] content = new byte[4096];
        while(input.read(content) > 0) {
            output.write(content);
        }

        serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));
        output.close();
        input.close();
    }

    public void exit() {
        Stage currentStage = Main.getPrimaryStage();
        currentStage.close();
    }
}
