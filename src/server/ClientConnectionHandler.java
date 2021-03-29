package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class ClientConnectionHandler implements Runnable {

    private Socket socket = null;
//    private SocketAddress socketAddress;
    private File FILE_TO_SEND = new File("ServerFiles/");
//    private File FILE_TO_GET = new File("ClientFiles/");
//
//    private BufferedReader bufferedReader = null;
//    private FileInputStream requestInput = null;
//    private PrintWriter outputStream = null;
    private FileInputStream requestInput = null;
    private BufferedInputStream bufferInputStream = null;
    private OutputStream outputStream = null;

//    private BufferedReader requestInput = null;
//    private DataOutputStream responseOutput = null;

    public ClientConnectionHandler(Socket socket) throws IOException {
        this.socket = socket;
//        this.socketAddress = socket.getLocalSocketAddress();
//        this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.requestInput = new FileInputStream(String.valueOf(socket.getInputStream()));
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
            handleRequests();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                requestInput.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void sendFile(File baseDir) throws IOException {
//        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
//        String fileName = inputStream.readUTF();
//
//        OutputStream oS = new FileOutputStream((baseDir + "/" + fileName));
//        byte[] content = new byte[(int)fileName.length()];
//
//
//    }

    private void handleRequests() throws IOException {
        try {
            // get client input stream
            File myFile = new File(FILE_TO_SEND.getName());
            byte[] content = new byte[(int) myFile.length()];

            requestInput = new FileInputStream(myFile);
            bufferInputStream = new BufferedInputStream(requestInput);
            bufferInputStream.read(content, 0, content.length);

            outputStream = socket.getOutputStream();

            System.out.println("Sending " + FILE_TO_SEND.getName() + "( " + content.length + " )" + " bytes");
            outputStream.write(content, 0, content.length);
            outputStream.flush();
            System.out.println("Done.");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferInputStream.close();
                outputStream.close();
                socket.close();
            } catch (IOException e) {

            }
        }
    }
}
