import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
  public static void main(String[] args) throws IOException {
    ServerSocket server = new ServerSocket(8080);

    while (true) {
      try {
        System.out.println("waiting for client to join...");
        Socket clientSocket = server.accept();
        Thread thread = new Thread(new ConnectionHandler(clientSocket));
        thread.start();
      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Failure! Error occurred.");
      }
    }
  }
}