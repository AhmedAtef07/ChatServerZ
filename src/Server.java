import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahmedatef on 22/03/17.
 */
public class Server {

    static List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7878);

        while(true) {
            Socket socket = serverSocket.accept();
            sockets.add(socket);
            Listener listener = new Listener(socket);
            listener.start();
        }
    }

    static void send(Socket socket, String message) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
        byte[] bytes = message.getBytes();
        dataOutputStream.writeInt(bytes.length);
        dataOutputStream.write(bytes);
    }

    static class Listener extends Thread {
        private Socket socket;

        public Listener(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while(true) {
                try {
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    int msgLength = dataInputStream.readInt();
                    byte[] msgBytes = new byte[msgLength];
                    dataInputStream.read(msgBytes);
                    String msg = new String(msgBytes);
                    System.out.println(msg);

                    for (Socket s : sockets) {
                        send(s, "Message Received: " + msg);
                    }
                } catch (IOException e) {
                    interrupt();
                }
            }
        }
    }
}
