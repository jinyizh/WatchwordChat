import java.io.IOException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class ConnectionHandler implements Runnable {

  private Socket clientSocket;

  public ConnectionHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  @Override
  public void run() {
    MyHttpRequest request = new MyHttpRequest(clientSocket);
    try {
      MyHttpResponse response = new MyHttpResponse(request);
      clientSocket.close();
    } catch (IOException | InterruptedException | NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }
}
