import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 9090);
        System.out.println("Connected to server");
        
        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String message = inputStream.readUTF();
                        System.out.println("Message received: " + message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        
