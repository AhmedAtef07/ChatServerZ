import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ahmedatef on 22/03/17.
 */
public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 7878);

        Listener listener = new Listener(socket);
        listener.start();

        while(true) {
            // Writing
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            String msg = new Scanner(System.in).nextLine();
            byte[] bytes = msg.getBytes();
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bytes);
        }

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
                } catch (IOException e) {
                    interrupt();
                }
            }
        }
    }
}
