package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 1. server is running
 * 2. user starts main.java.client
 * 3. user clicks on local file
 * 4. user clicks "upload"
 * 5. main.java.client connects to server
 * 6. main.java.client sends selected file to server
 * 7. server receives file
 * 8. server parses response and creates file in shared directory
 * 9. server ends connection
 */

public class Server {
    private ServerSocket socket = null;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        socket = new ServerSocket(port);
    }

    public void handleRequests() throws IOException {
        System.out.printf("Server is listening on port %d.\n", this.port);
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        while(true) {
            Socket acceptedSocket = socket.accept();
            if (acceptedSocket.isConnected()) {
                threadPool.execute(new server.ClientConnectionHandler(acceptedSocket));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;
        try {
            Server server = new Server(port);
            server.handleRequests();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}