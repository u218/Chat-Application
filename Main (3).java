import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static List<Socket> clientSockets = new ArrayList<>();
    
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9090);
        System.out.println("Server started on port 9090");
        
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected: " + socket);
            
            clientSockets.add(socket);
            new Thread(new ClientHandler(socket)).start();
        }
    }
    
    private static class ClientHandler implements Runnable {
        private Socket socket;
        private DataInputStream inputStream;
        private DataOutputStream outputStream;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                inputStream = new DataInputStream(socket.getInputStream());
                outputStream = new DataOutputStream(socket.getOutputStream());
                
                while (true) {
                    String message = inputStream.readUTF();
                    System.out.println("Message received: " + message);
                    
                    for (Socket clientSocket : clientSockets) {
                        if (clientSocket != socket) {
                            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                            out.writeUTF(message);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Client disconnected: " + socket);
                clientSockets.remove(socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

