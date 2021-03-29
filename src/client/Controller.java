package client;

import java.io.IOException;
import java.net.Socket;

public class Controller {
    private final int PORT = 8080;
    private String hostName = "localhost";
    private Socket socket;

    public void initialize() {
        try {
            socket = new Socket(hostName, PORT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
