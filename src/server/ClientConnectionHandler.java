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
    private final File file = new File("ServerDownload/");

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

            //System.out.println(inputStream.nextLine());

//            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            objInputStream = new ObjectInputStream(socket.getInputStream());

            String line = inputStream.nextLine();
            System.out.println(line);
            if (line.equals("UPLOAD")) {
                System.out.println("Please Work");
                download(socket, this.file);
            } else if (line.equals("DOWNLOAD")) {
                System.out.println("Please Work!");
                //download(socket, this.file);
            }
            //download(socket, this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFile() throws IOException {
        outputStream = new PrintWriter(socket.getOutputStream());
        outputStream.println(this.file.getName());
    }

    public void download(Socket socket, File file) throws IOException {
        DataInputStream input = new DataInputStream(socket.getInputStream());
        FileOutputStream output = new FileOutputStream(file + "/server.txt");
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
