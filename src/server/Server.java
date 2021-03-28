package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private final int PORT;

    private File serverFile = new File("serverFile/");
    private File clientFile = new File("clientFile/");

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.PORT = port;
    }

    public void handleRequests() throws IOException {
        System.out.printf("Server is listening to port: %d", this.PORT);

        // creating a thread to handle each of the clients
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ConnectionHandler handler = new ConnectionHandler(clientSocket);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
        }
    }
}
