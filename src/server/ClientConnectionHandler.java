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
    private final File serverPath = new File("ServerDownload/");
    private final File clientPath = new File("ClientDownload/");


    private final String serverFile = "server.txt";
    private final String clientFile = "client.txt";

    private PrintWriter outputStream;
    private ObjectInputStream objInputStream;
    private BufferedReader reader;

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
        try {
            Scanner inputStream = new Scanner(socket.getInputStream());
            PrintWriter outputStream = new PrintWriter(socket.getOutputStream(), true);

            String line = inputStream.nextLine();
            System.out.println(line);

//            switch (line) {
//                case "UPLOAD" -> {
//                    System.out.println("Please work!");
//                    sendFile(socket, this.serverPath, serverFile);
//                }
//                case "DOWNLOAD" -> {
//                    System.out.println("Please work!");
//                    sendFile(socket, this.clientPath, clientFile);
//                }
//            }

            if (line.equals("UPLOAD")) {
                System.out.println("Please Work");
                sendFile(socket, this.serverPath, serverFile);
            } else if (line.equals("DOWNLOAD")) {
                System.out.println("Please Work!");
                sendFile(socket, this.clientPath, clientFile);
            }
            inputStream.close();
            //download(socket, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFile(Socket socket, File file, String sendPath) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        FileOutputStream output = new FileOutputStream(file + "/" + sendPath);
        byte[] content = new byte[4096];

        int fileSize = 15123;
        int read = 0;
        int totalRead = 0;
        int remaining = fileSize;

        while((read = input.read(content, 0, Math.min(content.length, remaining))) > 0) {
            totalRead += read;
            remaining -= read;
            System.out.println("read " + totalRead + " bytes.");
            output.write(content, 0, read);
        }
        output.close();
        input.close();
    }
}