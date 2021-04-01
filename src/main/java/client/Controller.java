package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.*;
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
    @FXML private Button DownloadButton;

    public Controller() throws IOException { /* */ }

    @FXML
    MenuItem exitClient;

    public void initialize() {
        clientFiles.setItems(FXCollections.observableArrayList(clientDir.list()));
        serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));

        exitClient.setOnAction(actionEvent -> exit());

        UploadButton.setOnAction(actionEvent -> {
            try {
                upload("main.java.client.txt");
                serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        DownloadButton.setOnAction(actionEvent -> {
            try {
                download("server.txt");
                clientFiles.setItems(FXCollections.observableArrayList(clientDir.list()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Copy's a file from the main.java.client's local shared folder to the server's remote shared folder
     * @param file
     * @throws IOException
     */
    public void upload(String file) throws IOException {
        sendUploadMessageToServer();
        var output = new DataOutputStream(socket.getOutputStream());
        var input = new FileInputStream(clientDir + "/" + file);

        int character;

        while((character = input.read()) != -1) {
            output.write((char) character);
        }

        output.close();
        input.close();
        refresh();
    }

    /**
     * Will cause the file selected in the right list to transfer from the remote server's shared
     * folder to the local main.java.client's shared folder
     * @param file
     * @throws IOException
     */
    public void download(String file) throws IOException {
        sendDownloadMessageToServer();
        var output = new DataOutputStream(socket.getOutputStream());
        FileReader reader = new FileReader(serverDir + "/" + file);
        int character;
        while ((character = reader.read()) != -1) {
            output.write((char) character);
            System.out.print((char) character);
        }
        reader.close();
        output.close();
        refresh();
    }

    /**
     * Will cause the file selected in the right list to transfer from the remote server's shared
     * folder to the local main.java.client's shared folder
     * @param file
     * @throws IOException
     */
    public void downloadDeprecrated(String file) throws IOException {
        sendDownloadMessageToServer();

        var output = new DataOutputStream(socket.getOutputStream());
        var input = new FileInputStream(serverDir + "/" + file);

        byte[] content = new byte[4096];
        while(input.read(content) > 0) {
            output.write(content);
        }

        output.close();
        input.close();
        refresh();
    }

    /**
     * Send upload message to server, telling the server to perform the upload operation when the main.java.client hits upload
     * @throws IOException
     */
    public void sendUploadMessageToServer() throws IOException {
        PrintWriter output = null;
        output = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        output.println("UPLOAD");
        output.flush();
    }

    /**
     * Send download message to server, telling the server to perform the download operation when the main.java.client hits download
     * @throws IOException
     */
    public void sendDownloadMessageToServer() throws IOException {
        PrintWriter output = null;
        output = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        output.println("DOWNLOAD");
        output.flush();
    }

    public void refresh() {
        Stage currentStage = client.Main.getPrimaryStage();
        currentStage.hide();
        try {
            Stage newStage = new Stage();
            client.Main.setPrimaryStage(newStage);
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            newStage.setScene(new Scene(root, 500, 500));
            newStage.setTitle("File Sharing");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void exit() {
        Stage currentStage = client.Main.getPrimaryStage();
        currentStage.close();
    }
}
