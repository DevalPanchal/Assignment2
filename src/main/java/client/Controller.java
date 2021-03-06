package client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
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
    // this is the shared folder aka server
    @FXML private File serverDir = new File(client.Main.getSharedFileDestination());

    @FXML private Button UploadButton;
    @FXML private Button DownloadButton;
    @FXML private Button refreshWindow;

    @FXML private TextField computerName;

    public String serverFileName;
    public String clientFileName;

    /**
     * Controller constructor
     * @throws IOException
     */
    public Controller() throws IOException { /* */ }

    @FXML MenuItem exitClient;
    @FXML MenuItem chooseFolder;

    /**
     * Runs when the UI is instantiated
     */
    public void initialize() {
        // set the computer name
        computerName.setText(client.Main.getComputerName());
        // setting the list view to the respective files in the directory
        clientFiles.setItems(FXCollections.observableArrayList(clientDir.list()));
        serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));

        // exit event
        exitClient.setOnAction(actionEvent -> exit());

        // handle upload on click
        UploadButton.setOnAction(actionEvent -> {
            try {
                upload(clientFileName);
                serverFiles.setItems(FXCollections.observableArrayList(serverDir.list()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // handle download on click
        DownloadButton.setOnAction(actionEvent -> {
            try {
                download(serverFileName);
                clientFiles.setItems(FXCollections.observableArrayList(clientDir.list()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // handle the click on the files on the server [right listview]
        serverFiles.setOnMouseClicked(actionEvent -> {
            serverFileName = serverFiles.getSelectionModel().getSelectedItem();
            //System.out.println(serverFiles.getSelectionModel().getSelectedItem());
        });

        // handle the click on the files on the client [left listview]
        clientFiles.setOnMouseClicked(actionEvent -> {
            clientFileName = clientFiles.getSelectionModel().getSelectedItem();
        });

        // handle the directory chooser
        chooseFolder.setOnAction(actionEvent -> {
            try {
                Dir();
                clientFiles.setItems(FXCollections.observableArrayList(serverDir.list()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // refreshes the window
        refreshWindow.setOnAction(actionEvent -> refresh());
    }

    /**
     * Copy's a file from the main.java.client's local shared folder to the server's remote shared folder [left to right]
     * @param file
     * @throws IOException
     */
    public void upload(String file) throws IOException {
        sendUploadMessageToServer();
        if (file == null) {
            file = "client.txt";
        }

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
     * Will cause the file selected in the right list to transfer from the remote server's shared [right to left]
     * folder to the local main.java.client's shared folder
     * @param file
     * @throws IOException
     */
    public void download(String file) throws IOException {
        sendDownloadMessageToServer();
        if (file == null) {
            file = "server.txt";
        }
        var output = new DataOutputStream(socket.getOutputStream());
        FileReader reader = new FileReader(serverDir + "/" + file);

        int character;
        while ((character = reader.read()) != -1) {
            output.write((char) character);
            //System.out.print((char) character);
        }
        reader.close();
        output.close();
        refresh();
    }

    /**
     * Choose the directory that the user wants to send the files from
     * @throws IOException
     */
    public void Dir() throws IOException {
        sendDirMessageToServer();
        DirectoryChooser dirChooser = new DirectoryChooser();
//        FileChooser chooseFile = new FileChooser();
        serverDir = dirChooser.showDialog(client.Main.getPrimaryStage());
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
        output.println(client.Main.getSharedFileDestination());
        output.println(clientFileName);
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
        output.println(client.Main.getSharedFileDestination());
        output.println(serverFileName);
        output.flush();
    }

    /**
     * Send dir message to server telling the server to return the list
     * @throws IOException
     */
    public void sendDirMessageToServer() throws IOException {
        PrintWriter output = null;
        output = new PrintWriter(new BufferedOutputStream(socket.getOutputStream()));
        output.println("DIR");
        output.println(serverDir);
        output.flush();
    }

    /**
     * Refreshes the entire window page effectively making a new window with updated listviews
     */
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

    /**
     * Exits the current stage
     */
    public void exit() {
        Stage currentStage = client.Main.getPrimaryStage();
        currentStage.close();
    }

}