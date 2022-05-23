import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleSingleThreadConsoleTCPClient {
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8189;
    private DataInputStream in;
    private DataOutputStream out;
    private Thread clientThread;

    public static void main(String[] args) {
        new SimpleSingleThreadConsoleTCPClient().start();
    }

    private void start() {
        try (Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Connected to server");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            startConsoleInput();

            while (!socket.isClosed()) {
                String income = in.readUTF();
                System.out.println("Received: " + income);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shutdown() throws IOException {
        if (clientThread != null && clientThread.isAlive()) {
            clientThread.interrupt();
        }
        System.out.println("Client stopped");
    }

    private void startConsoleInput() {
        clientThread = new Thread(() -> {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                System.out.println("Enter your message >>>>> ");
                while (!Thread.currentThread().isInterrupted()) {
                    if (br.ready()) {
                        String outcome = br.readLine();
                        out.writeUTF(outcome);

                        if (outcome.equals("/end")) {
                            shutdown();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

}