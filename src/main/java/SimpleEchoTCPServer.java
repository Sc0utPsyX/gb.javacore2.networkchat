import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleEchoTCPServer {
    private static final int PORT = 8189;
    private static final int CONNECTIONS = 3;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server Started");
            Socket[] socket = new Socket[CONNECTIONS];
            Thread[] thread = new Thread[CONNECTIONS];
            DataOutputStream[] out = new DataOutputStream[CONNECTIONS];
            for (int i = 0; i < CONNECTIONS; i++) {
                int num = i;
                thread[i] = new Thread(() -> {
                    try {
                        System.out.println("Client connected");
                        DataInputStream in = new DataInputStream(socket[num].getInputStream());
                        out[num] = new DataOutputStream(socket[num].getOutputStream());
                        while (true) {
                            String income = in.readUTF();
                            System.out.println("Received: " + income);
                            Thread.sleep(50);
                            for (int j = 0; j < CONNECTIONS; j++) {
                                if (out[j] == null) {} else out[j].writeUTF("Client #" + (num + 1) + ": " + income);
                            }
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
            int j = 0;
            while(j < CONNECTIONS){
                socket[j] = serverSocket.accept();
                thread[j].start();
                j++;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
