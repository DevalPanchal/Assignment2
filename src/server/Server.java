package server;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket = null;
    private int PORT;

    public Server(int PORT) throws IOException {
        serverSocket = new ServerSocket(PORT);
        this.PORT = PORT;
    }

    public void handleRequests() throws IOException {
        System.out.printf("Server is listening on PORT: %d\n", this.PORT);
        // creating a thread to handle each of the clients
        while(true) {
            Socket clientSocket = serverSocket.accept();
            ClientConnectionHandler handler = new ClientConnectionHandler(clientSocket);
            Thread handlerThread = new Thread(handler);
            handlerThread.start();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = 8080;

        if (args.length > 1) {
            port = Integer.parseInt(args[0]);
        }

        // instantiate the Server class
        Server server = new Server(port);

        server.handleRequests();
    }
}
