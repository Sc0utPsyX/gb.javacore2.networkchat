import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleEchoTCPServer {
    private static final int PORT = 8189;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            Socket socket = serverSocket.accept();
            System.out.println("Client connected");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            while (true) {
                String income = in.readUTF();
                System.out.println("Received: " + income);
                Thread.sleep(50);
                out.writeUTF("ECHO: " + income);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}