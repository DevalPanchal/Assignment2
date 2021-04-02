package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class ClientConnectionHandler implements Runnable {
    private Socket socket = null;

    private File serverPath = null;
    private File clientPath = new File("ClientDownload/");

    private final String serverFile = "server.txt";
    private final String clientFile = "client.txt";

    private PrintWriter outputStream;
    private ObjectInputStream objInputStream;
    private BufferedReader reader;

    private String testClientDynamic;
    private String testServerDynamic;

    private int ThresholdSize = 5000;

    /**
     * ClientConnectionHandler Constructor
     * @param socket
     * @throws IOException
     */
    public ClientConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        //clientPath = new File(client.Main.getFileDestination());
        try {
            Scanner inputStream = new Scanner(socket.getInputStream());
            PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);

            String line = inputStream.next();
            serverPath = new File(inputStream.next());
            testClientDynamic = inputStream.next();
            testServerDynamic = testClientDynamic;

            System.out.println(line);
            System.out.println(clientPath);
            System.out.println(testClientDynamic);
            System.out.println(testServerDynamic);

            switch(line) {
                case "UPLOAD" :
                    sendFile(socket, this.serverPath, testServerDynamic);
                    socket.close();
                case "DOWNLOAD":
                    sendFile(socket, this.clientPath, testClientDynamic);
                    socket.close();
                case "DIR":
                    socket.close();
                case "" :
                    inputStream.close();
                    socket.close();
                default :
                    inputStream.close();
                    socket.close();
            }

//            if (line.equals("UPLOAD")) {
//                System.out.println(clientPath);
//                sendFile(socket, this.serverPath, serverFile);
//            } else if (line.equals("DOWNLOAD")) {
//                System.out.println(clientPath);
//                sendFile(socket, this.clientPath, clientFile);
//            } else {
//                inputStream.close();
//                socket.close();
//            }
            //download(socket, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendFile(Socket socket, File file, String sendPath) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        FileOutputStream output = new FileOutputStream(file + "/" + sendPath);

        byte[] content = new byte[ThresholdSize];

        int fileSize = ThresholdSize;
        int read = 0;
        int remainingBytes = fileSize;

        while((read = input.read(content, 0, content.length)) > 0) {
            remainingBytes -= read;
            output.write(content, 0, read);
        }

        output.close();
        input.close();
    }
}