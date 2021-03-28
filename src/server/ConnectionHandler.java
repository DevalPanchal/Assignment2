package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private Socket socket = null;
    // request from the server (get, post) -> REST architecture
    private BufferedReader req = null;
    // response from server
    private DataOutputStream res = null;

    public ConnectionHandler(Socket socket) throws IOException {
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

    }
}
